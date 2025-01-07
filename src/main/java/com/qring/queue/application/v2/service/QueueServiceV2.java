package com.qring.queue.application.v2.service;

import com.qring.queue.application.v2.message.redis.RedisMessagePublisherV2;
import com.qring.queue.application.v2.res.QueueGetResDTOV2;
import com.qring.queue.infrastructure.messaging.dto.ReservationCreateEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public QueueGetResDTOV2 getBy(Long restaurantId, Long reservationId) {

        return redisMessagePublisherV2.getQueueInfoByRestaurantIdAndReservationId(restaurantId, reservationId);
    }

    // 대기열에서 제거
    public void deleteBy(Long restaurantId, Long reservationId) {

        redisMessagePublisherV2.removeQueueByRestaurantIdAndReservationId(restaurantId, reservationId);
    }

    // ============== 대기 순번 알림 발송 관련 ================
    // 식당별 대기열 리스트 조회
    public List<Long> getWaitingListBy(Long restaurantId) {
        String key = "waiting_list" + restaurantId; // Redis Key 생성
        Set<Object> waitingList = redisMessagePublisherV2.getFromWaitingList(key); // Redis에서 모든 데이터 조회
        if (waitingList.isEmpty()) {
            log.info("대기열이 비어있습니다.");
            return Collections.emptyList();
        }

        return waitingList.stream()
                .map(obj -> Long.valueOf(obj.toString())) // Object -> Long 변환
                .sorted() // 순서를 보장
                .toList();
    }
}
