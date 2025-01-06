package com.qring.queue.application.v2.message.redis;

import com.qring.queue.application.v2.res.QueueGetResDTOV2;

public interface RedisMessagePublisherV2 {

    void addWaitingListByRestaurantIdAndReservationId(Long restaurantId, Long reservationId);

    QueueGetResDTOV2 getQueueInfoByRestaurantIdAndReservationId(Long restaurantId, Long reservationId);
}
