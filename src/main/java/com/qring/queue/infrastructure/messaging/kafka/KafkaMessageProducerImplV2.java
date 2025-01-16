package com.qring.queue.infrastructure.messaging.kafka;

import com.qring.queue.application.v2.message.kafka.KafkaMessageProducerV2;
import com.qring.queue.infrastructure.messaging.dto.QueueAlarmEventDTOV2;
import com.qring.queue.infrastructure.messaging.dto.CreateReservationMessageDTOV2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaMessageProducerImplV2 implements KafkaMessageProducerV2 {

    @Value("${spring.kafka.topic.queue-alarm-event}")
    private String queueAlarmEvent;

    @Value("${spring.kafka.topic.queue-reservation-event}")
    private String queueReservationEvent;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishQueueAlarmEvent(Long reservationId) {

        QueueAlarmEventDTOV2 event = new QueueAlarmEventDTOV2(reservationId);
        kafkaTemplate.send(queueAlarmEvent, event);
    }

    public void publishReservationAndQueueEvent(CreateReservationMessageDTOV2 event) {

        kafkaTemplate.send(queueReservationEvent, event);
    }
}
