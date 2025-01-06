package com.qring.queue.infrastructure.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qring.queue.application.v2.message.kafka.KafkaMessageConsumerV2;
import com.qring.queue.infrastructure.messaging.dto.ReservationCreateEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "KafkaMessageConsumerV2 Log")
public class KafkaMessageConsumerImplV2 implements KafkaMessageConsumerV2 {

    // Kafka 메시지 처리
    @KafkaListener(topics = "reservation-create-event-topic", groupId = "${spring.kafka.consumer.group-id}")
    public ReservationCreateEventDTO handleMessage(String message) {
        try {
            // 메시지에서 예약 ID와 식당 ID 추출
            return parseMessage(message);
        } catch (Exception e) {
            log.error("메시지 추출 실패 : {}", message, e);
            throw new RuntimeException("Kafka 메시지 처리 실패: " + message, e);
        }
    }

    // 예약, 식당 아이디 추출
    private ReservationCreateEventDTO parseMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(message, ReservationCreateEventDTO.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid message format: " + message, e);
        }
    }
}
