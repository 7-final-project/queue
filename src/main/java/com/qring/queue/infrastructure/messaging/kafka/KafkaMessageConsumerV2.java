package com.qring.queue.infrastructure.messaging.kafka;

import com.qring.queue.application.global.exception.ErrorCode;
import com.qring.queue.application.global.exception.QueueException;
import com.qring.queue.application.v2.message.kafka.KafkaMessageProducerV2;
import com.qring.queue.application.v2.res.QueueGetResDTOV2;
import com.qring.queue.application.v2.service.QueueServiceV2;
import com.qring.queue.infrastructure.messaging.dto.ReservationAndQueueEventDTOV2;
import com.qring.queue.infrastructure.messaging.dto.ReservationEventDTOV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "KafkaMessageConsumerV2 Log")
public class KafkaMessageConsumerV2 {

    private final QueueServiceV2 queueServiceV2;
    private final KafkaMessageProducerV2 kafkaMessageProducerV2;

    // Kafka 메시지 처리
    @KafkaListener(topics = "reservation-create-event-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void extractIdListBy(ReservationEventDTOV2 message) {
        try {
            QueueGetResDTOV2.QueueInfo dto = queueServiceV2.getBy(
                    message.getRestaurantId(),
                    message.getReservationId())
                    .getQueueInfo();

            // 새로운 대기 정보를 포함한 DTO 생성
            ReservationAndQueueEventDTOV2 event = ReservationAndQueueEventDTOV2.from(
                    message,
                    dto.getSequence(),
                    dto.getTeamsAhead()
                    );

            // 메시지 서비스로 전송
            kafkaMessageProducerV2.publishReservationAndQueueEvent(event);
        } catch (Exception e) {
            log.error("메시지 추출 실패 : {}", message, e);
            throw new QueueException(ErrorCode.BAD_REQUEST_ERROR, "메세지 추출에 실패하였습니다.");
        }
    }
}
