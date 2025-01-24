package com.qring.queue.infrastructure.messaging.kafka;

import com.qring.queue.application.messaging.kafka.KafkaMessageProducerV2;
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

    @Value("${spring.kafka.topic.queue-create-fail-event}")
    private String queueCreateFailEvent;

    @Value("${spring.kafka.topic.queue-delete-fail-event}")
    private String queueDeleteFailEvent;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishQueueAlarmEvent(Long reservationId) {

        QueueAlarmEventDTOV2 event = new QueueAlarmEventDTOV2(reservationId);
        kafkaTemplate.send(queueAlarmEvent, event);
    }

    public void publishReservationAndQueueEvent(CreateReservationMessageDTOV2 event) {

        kafkaTemplate.send(queueReservationEvent, event);
    }

    public void publishQueueCreateFailEvent(Long reservationId) {

        kafkaTemplate.send(queueCreateFailEvent, reservationId);
    }

    public void publishQueueDeleteFailEvent(Long reservationId) {

        kafkaTemplate.send(queueDeleteFailEvent, reservationId);
    }
}
