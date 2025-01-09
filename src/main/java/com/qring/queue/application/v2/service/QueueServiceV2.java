package com.qring.queue.application.v2.service;

import com.qring.queue.application.v2.message.redis.RedisMessagePublisherV2;
import com.qring.queue.application.v2.res.QueueGetResDTOV2;
import com.qring.queue.infrastructure.messaging.dto.ReservationCreationEventDTOV2;
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
    public void enrollWaitingListByEvent(ReservationCreationEventDTOV2 event) {

        // Redis에 예약 정보 저장
        redisMessagePublisherV2.addWaitingListByRestaurantIdAndReservationId(
                event.getReservationInfo().getRestaurantId(),
                event.getReservationInfo().getId());

        log.info("대기열 등록 성공 : Restaurant ID = {}, Reservation ID = {}",
                event.getReservationInfo().getRestaurantId(),
                event.getReservationInfo().getId());
    }

    // 사용자 대기 순서 반환
    public QueueGetResDTOV2 getBy(Long restaurantId, Long reservationId) {

        return redisMessagePublisherV2.getQueueInfoByRestaurantIdAndReservationId(restaurantId, reservationId);
    }

    // 대기열에서 제거
    public void deleteBy(Long restaurantId, Long reservationId) {

        redisMessagePublisherV2.removeQueueByRestaurantIdAndReservationId(restaurantId, reservationId);
    }

    // ============== 대기 순번 알림 발송 관련 ================
    // 식당별 대기열 리스트 조회
    public List<Long> getWaitingListByRestaurantId(Long restaurantId) {
        String key = "waiting_list" + restaurantId; // Redis Key 생성
        Set<Object> waitingList = redisMessagePublisherV2.getFromWaitingListByKey(key); // Redis에서 모든 데이터 조회
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
