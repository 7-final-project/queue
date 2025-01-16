package com.qring.queue.application.v2.message.kafka;

import com.qring.queue.infrastructure.messaging.dto.CreateReservationMessageDTOV2;

public interface KafkaMessageProducerV2 {

    void publishQueueAlarmEvent(Long reservationId);

    void publishReservationAndQueueEvent(CreateReservationMessageDTOV2 event);
}
