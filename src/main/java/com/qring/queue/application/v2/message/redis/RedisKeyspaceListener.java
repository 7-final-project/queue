package com.qring.queue.application.v2.message.redis;

import com.qring.queue.application.v2.message.kafka.KafkaMessageProducerV2;
import com.qring.queue.application.v2.service.QueueServiceV2;
import com.qring.queue.infrastructure.messaging.dto.CreateReservationMessageDTOV3;
import com.qring.queue.infrastructure.util.EventSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisKeyspaceListener implements MessageListener {

    private final QueueServiceV2 queueService;
    private final KafkaMessageProducerV2 kafkaMessageProducerV2;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody());
        String event = new String(pattern);

        log.info("Redis 대기열 변동 이벤트 감지: key={}, event={}", key, event);

        // Key에서 식당 ID 추출
        Long restaurantId = extractRestaurantIdFromKey(key);

        if (event.equals("__keyevent@*__:zrem")) {
            handleZRemEvent(restaurantId);
        } else if (event.equals("reservation-create-event")) {
            queueService.enrollWaitingListByEventV2(EventSerializer.deserialize(key, CreateReservationMessageDTOV3.class));
        } else {
            log.warn("Redis 알 수 없는 이벤트 감지: {}", event);
        }
    }

    private void handleZRemEvent(Long restaurantId) {
        processFifthReservationEvent(restaurantId);
    }

    private void processFifthReservationEvent(Long restaurantId) {
        List<Long> waitingList = queueService.getWaitingListByRestaurantId(restaurantId);
        if (waitingList.size() >= 5) {
            Long fifthReservationId = waitingList.get(4); // 5번째 예약 ID

            sendQueueAlarmEvent(fifthReservationId); // Kafka 이벤트 발송

            log.info("Kafka 5번째 순번 예약 ID 이벤트 발송: 식당 ID = {}, 예약 ID = {}", restaurantId, fifthReservationId);

        }
    }

    private void sendQueueAlarmEvent(Long reservationId) {
        kafkaMessageProducerV2.publishQueueAlarmEvent(reservationId);
        log.info("Kafka 메시지 발행: {}", reservationId);
    }

    private Long extractRestaurantIdFromKey(String key) {
        // Key에서 숫자 ID를 추출 (예: "waiting_list123" -> 123)
        return Long.parseLong(key.replaceAll("\\D+", ""));
    }
}
