package com.qring.queue.application.v2.service;

import com.qring.queue.application.v2.message.redis.RedisMessagePublisherV2;
import com.qring.queue.application.v2.res.QueueGetResDTOV2;
import com.qring.queue.infrastructure.messaging.dto.ReservationCreateEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "QueueServiceV2 Log")
public class QueueServiceV2 {

    private final RedisMessagePublisherV2 redisMessagePublisherV2;

    // 대기열 등록
    public void enrollWaitingListByEvent(ReservationCreateEventDTO event) {

        // Redis에 예약 정보 저장
        redisMessagePublisherV2.addWaitingListByRestaurantIdAndReservationId(event.getRestaurantId(), event.getReservationId());

        log.info("대기열 등록 성공 : Restaurant ID = {}, Reservation ID = {}",
                event.getRestaurantId(), event.getReservationId());
    }

    // 사용자 대기 순서와 총 대기 인원 반환
    public QueueGetResDTOV2 getInfoByRestaurantIdAndReservationId(Long restaurantId, Long reservationId) {

        return redisMessagePublisherV2.getQueueInfoByRestaurantIdAndReservationId(restaurantId, reservationId);
    }
}
