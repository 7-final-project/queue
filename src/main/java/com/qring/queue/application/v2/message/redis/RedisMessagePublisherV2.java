package com.qring.queue.application.v2.message.redis;

import com.qring.queue.application.v2.res.QueueGetResDTOV2;

import java.util.Set;

public interface RedisMessagePublisherV2 {

    void addWaitingListByRestaurantIdAndReservationId(Long restaurantId, Long reservationId);

    QueueGetResDTOV2 getQueueInfoByRestaurantIdAndReservationId(Long restaurantId, Long reservationId);

    void removeQueueByRestaurantIdAndReservationId(Long restaurantId, Long reservationId);

    Set<Object> getFromWaitingListByKey(String key);

    String getLastSentIdByRestaurantId(Long restaurantId);

    void saveLastSentIdByRestaurantIdAndReservationId(Long restaurantId, String reservationId);

}
