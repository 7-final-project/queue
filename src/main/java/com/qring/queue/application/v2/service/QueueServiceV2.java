package com.qring.queue.application.v2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qring.queue.application.v2.res.QueueGetResDTOV2;
import com.qring.queue.infrastructure.messaging.ReservationCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueServiceV2 {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String WAITING_LIST_KEY_PREFIX = "waiting_list";

    // Kafka 메시지 처리
    @KafkaListener(topics = "reservation-create-event-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void handleMessage(String message) {
        try {
            // 메시지에서 예약 ID와 식당 ID 추출
            ReservationCreateEvent event = parseMessage(message);

            // Redis에 예약 정보 저장
            addWaitingListBy(event.getRestaurantId(), event.getReservationId());

            log.info("대기열 등록 성공 : Restaurant ID = {}, Reservation ID = {}",
                    event.getRestaurantId(), event.getReservationId());
        } catch (Exception e) {
            log.error("메시지 추출 실패 : {}", message, e);
        }
    }

    // 예약, 식당 아이디 추출
    private ReservationCreateEvent parseMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(message, ReservationCreateEvent.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid message format: " + message, e);
        }
    }

    // 사용자 대기 순서와 총 대기 인원 반환
    public QueueGetResDTOV2 getQueueInfoBy(Long restaurantId, Long reservationId) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

        // 식당 Id 에 해당하는 Key 생성
        String key = WAITING_LIST_KEY_PREFIX + restaurantId;

        // 사용자 대기 순서 조회
        int rank = Optional.ofNullable(zSetOps.rank(key, String.valueOf(reservationId)))
                .map(Long::intValue)
                .orElse(-1);

        // 대기열에 사용자가 없을 경우 등록
        if (rank == -1) {
            addWaitingListBy(restaurantId, reservationId);
            rank = getSeqBy(zSetOps, key, String.valueOf(reservationId));
        }

        // 총 대기 인원 조회
        int totalWaitingCount = Optional.ofNullable(zSetOps.zCard(WAITING_LIST_KEY_PREFIX))
                .orElse(0L) // 기본값 설정
                .intValue();

        // 대기 순서는 Redis의 rank가 0부터 시작하므로 1을 더해 반환
        return QueueGetResDTOV2.of(rank + 1, totalWaitingCount);
    }

    // 대기열에 등록
    private void addWaitingListBy(Long restaurantId, Long reservationId) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

        // 식당 Id 에 해당하는 Key 생성
        String key = WAITING_LIST_KEY_PREFIX + restaurantId;

        long score = System.currentTimeMillis();
        zSetOps.add(key, String.valueOf(reservationId), score);;
    }

    // 사용자 순서 조회 반환
    private int getSeqBy(ZSetOperations<String, Object> zSetOps, String key, String value) {
        return Optional.ofNullable(zSetOps.rank(key, value))
                .map(Long::intValue)
                .orElse(-1);
    }
}
