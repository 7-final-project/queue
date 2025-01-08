package com.qring.queue.application.v2.message.kafka;

public interface KafkaMessageProducerV2 {

    void publishQueueAlarmEvent(Long reservationId);
}
