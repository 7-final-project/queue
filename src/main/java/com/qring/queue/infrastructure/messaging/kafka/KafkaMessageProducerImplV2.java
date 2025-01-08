package com.qring.queue.infrastructure.messaging.kafka;

import com.qring.queue.application.v2.message.kafka.KafkaMessageProducerV2;
import com.qring.queue.infrastructure.messaging.dto.QueueAlarmEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaMessageProducerImplV2 implements KafkaMessageProducerV2 {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishQueueAlarmEvent(Long reservationId) {

        QueueAlarmEventDTO event = new QueueAlarmEventDTO(reservationId);
        kafkaTemplate.send("queue-alarm-event-topic", event);
    }
}
