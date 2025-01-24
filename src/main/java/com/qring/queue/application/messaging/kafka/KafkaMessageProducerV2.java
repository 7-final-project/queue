package com.qring.queue.application.messaging.kafka;

import com.qring.queue.infrastructure.messaging.dto.CreateReservationMessageDTOV2;

public interface KafkaMessageProducerV2 {

    void publishQueueAlarmEvent(Long reservationId);

    void publishReservationAndQueueEvent(CreateReservationMessageDTOV2 event);

    void publishQueueCreateFailEvent(Long reservationId);

    void publishQueueDeleteFailEvent(Long reservationId);
}
