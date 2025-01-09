package com.qring.queue.application.v2.message.kafka;

import com.qring.queue.infrastructure.messaging.dto.ReservationAndQueueEventDTOV2;

public interface KafkaMessageProducerV2 {

    void publishQueueAlarmEvent(Long reservationId);

    void publishReservationAndQueueEvent(ReservationAndQueueEventDTOV2 event);
}
