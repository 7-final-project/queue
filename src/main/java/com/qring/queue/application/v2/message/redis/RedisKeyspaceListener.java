package com.qring.queue.application.v2.message.redis;

import com.qring.queue.application.v2.service.QueueServiceV2;
import com.qring.queue.infrastructure.messaging.dto.QueueAlarmEventDTO;
import com.qring.queue.infrastructure.messaging.redis.RedisMessagePublisherImplV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisKeyspaceListener implements MessageListener {

    private final QueueServiceV2 queueService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RedisMessagePublisherImplV2 redisMessagePublisherV2;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody());
        String event = new String(pattern);

        log.info("Redis 대기열 변동 이벤트 감지: key={}, event={}", key, event);

        // Key에서 식당 ID 추출
        Long restaurantId = extractRestaurantIdFromKey(key);

        switch (event) {
            case "__keyevent@*__:zadd" -> handleZAddEvent(restaurantId);
            case "__keyevent@*__:zrem" -> handleZRemEvent(restaurantId);
            default -> log.warn("Redis 알 수 없는 이벤트 감지: {}", event);
        }
    }

    private void handleZAddEvent(Long restaurantId) {
        processFifthReservationEvent(restaurantId);
    }

    private void handleZRemEvent(Long restaurantId) {
        processFifthReservationEvent(restaurantId);
    }

    private void processFifthReservationEvent(Long restaurantId) {
        List<Long> waitingList = queueService.getWaitingListBy(restaurantId);
        if (waitingList.size() >= 5) {
            Long fifthReservationId = waitingList.get(4); // 5번째 예약 ID
            // Redis에서 마지막 발송된 ID 확인
            String lastSentId = redisMessagePublisherV2.getLastSentId(restaurantId);

            // 5번째 ID가 이전에 발송된 ID와 다를 경우에만 이벤트 발송
            if (!fifthReservationId.toString().equals(lastSentId)) {
                sendQueueAlarmEvent(fifthReservationId); // Kafka 이벤트 발송

                // Redis에 발송된 ID 저장
                redisMessagePublisherV2.saveLastSentId(restaurantId, String.valueOf(fifthReservationId));
                log.info("Kafka 5번째 순번 예약 ID 이벤트 발송: 식당 ID = {}, 예약 ID = {}", restaurantId, fifthReservationId);
            } else {
                log.info("Redis 중복 이벤트 방지: 식당 ID = {}, 예약 ID = {}", restaurantId, fifthReservationId);
            }
        }
    }

    private void sendQueueAlarmEvent(Long reservationId) {
        QueueAlarmEventDTO event = new QueueAlarmEventDTO(reservationId);
        kafkaTemplate.send("queue-alarm-event-topic", event);
        log.info("Kafka 메시지 발행: {}", reservationId);
    }

    private Long extractRestaurantIdFromKey(String key) {
        // Key에서 숫자 ID를 추출 (예: "waiting_list123" -> 123)
        return Long.parseLong(key.replaceAll("\\D+", ""));
    }
}
