package com.qring.queue.application.v2.message.kafka;

import com.qring.queue.infrastructure.messaging.dto.ReservationCreateEventDTO;

public interface KafkaMessageConsumerV2 {
    ReservationCreateEventDTO handleMessage(String message);
}
