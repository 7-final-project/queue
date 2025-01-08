package com.qring.queue.infrastructure.messaging.redis;

import com.qring.queue.application.v2.message.redis.RedisMessagePublisherV2;
import com.qring.queue.application.v2.res.QueueGetResDTOV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "RedisMessagePublisherV2 Log")
public class RedisMessagePublisherImplV2 implements RedisMessagePublisherV2 {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String WAITING_LIST_KEY_PREFIX = "waiting_list";
    private static final String LAST_SENT_KEY_PREFIX = "last_sent_event:";

    // 대기열에 등록
    public void addWaitingListByRestaurantIdAndReservationId(Long restaurantId, Long reservationId) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

        // 식당 Id 에 해당하는 Key 생성
        String key = WAITING_LIST_KEY_PREFIX + restaurantId;

        long score = System.currentTimeMillis();
        zSetOps.add(key, String.valueOf(reservationId), score);
    }

    // 사용자 대기 순서와 총 대기 인원 반환
    public QueueGetResDTOV2 getQueueInfoByRestaurantIdAndReservationId(Long restaurantId, Long reservationId) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

        // 식당 Id 에 해당하는 Key 생성
        String key = WAITING_LIST_KEY_PREFIX + restaurantId;

        // 사용자 대기 순서 조회
        int rank = Optional.ofNullable(zSetOps.rank(key, String.valueOf(reservationId)))
                .map(Long::intValue)
                .orElseThrow(() -> new IllegalArgumentException("대기열에 존재하지 않거나 이미 삭제되었습니다."));

        // ========== 추후 미루기 기능에 사용될 것 같아 삭제하지 않고 주석 처리 ===========
//        // 대기열에 사용자가 없을 경우 등록
//        if (rank == -1) {
//            addWaitingListByRestaurantIdAndReservationId(restaurantId, reservationId);
//            rank = getRankByKeyAndValue(zSetOps, key, String.valueOf(reservationId));
//        }
        // ===================================================================

        QueueGetResDTOV2.QueueInfo queueInfo = QueueGetResDTOV2.QueueInfo.from(
                rank + 1,
                rank);


        // 대기 순서는 Redis의 rank가 0부터 시작하므로 1을 더해 반환
        return QueueGetResDTOV2.of(queueInfo);
    }

    // 사용자 순서 조회 반환
    private int getRankByKeyAndValue(ZSetOperations<String, Object> zSetOps, String key, String value) {
        return Optional.ofNullable(zSetOps.rank(key, value))
                .map(Long::intValue)
                .orElse(-1);
    }

    // 대기열에서 제거
    public void removeQueueByRestaurantIdAndReservationId(Long restaurantId, Long reservationId) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

        String key = WAITING_LIST_KEY_PREFIX + restaurantId;

        zSetOps.remove(key, String.valueOf(reservationId));
    }

    // ============ 대기 순번 알림 발송 관련 ================
    // 대기열 정보 전체 가져오기
    public Set<Object> getFromWaitingListByKey(String key) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        return zSetOps.range(key, 0, -1); // ZSet의 모든 데이터를 가져옴
    }
    // 마지막 발송 ID 조회
    public String getLastSentIdByRestaurantId(Long restaurantId) {
        String key = LAST_SENT_KEY_PREFIX + restaurantId;
        return (String) redisTemplate.opsForValue().get(key);
    }

    // 마지막 발송 ID 저장
    public void saveLastSentIdByRestaurantIdAndReservationId(Long restaurantId, String reservationId) {
        String key = LAST_SENT_KEY_PREFIX + restaurantId;
        redisTemplate.opsForValue().set(key, reservationId);
    }

}
