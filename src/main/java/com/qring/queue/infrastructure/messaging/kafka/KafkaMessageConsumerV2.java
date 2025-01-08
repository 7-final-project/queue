package com.qring.queue.infrastructure.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qring.queue.application.global.exception.ErrorCode;
import com.qring.queue.application.global.exception.QueueException;
import com.qring.queue.application.v2.message.kafka.KafkaMessageProducerV2;
import com.qring.queue.application.v2.res.QueueGetResDTOV2;
import com.qring.queue.application.v2.service.QueueServiceV2;
import com.qring.queue.infrastructure.messaging.dto.ReservationAndQueueEventDTOV2;
import com.qring.queue.infrastructure.messaging.dto.ReservationCreateEventDTOV2;
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
    private final ObjectMapper objectMapper;

    /**
     * 예약 생성 이벤트 소비
     * @param message
     */
    @KafkaListener(topics = "reservation-create-event-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void extractReservationInfoBy(String message) {
        try {
            ReservationEventDTOV2 parsedMessage = objectMapper.readValue(message, ReservationEventDTOV2.class);

            queueServiceV2.enrollWaitingListByEvent(parsedMessage);

            QueueGetResDTOV2.QueueInfo dto = queueServiceV2.getBy(
                            parsedMessage.getReservationInfo().getRestaurantId(),
                            parsedMessage.getReservationInfo().getId())
                            .getQueueInfo();

            // 새로운 대기 정보를 포함한 DTO 생성
            ReservationAndQueueEventDTOV2 event = ReservationAndQueueEventDTOV2.from(
                    parsedMessage,
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

    /**
     * 예약 입장 or 취소 이벤트 소비
     * @param message
     */
    @KafkaListener(topics = "reservation-update-event-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void handleReservationCancellation(String message) {
        try {
            // 메시지에서 예약 ID와 식당 ID 추출
            ReservationCreateEventDTOV2 event = parseMessage(message);
            queueServiceV2.deleteBy(event.getRestaurantId(), event.getReservationId());
        } catch (Exception e) {
            log.error("메시지 추출 실패 : {}", message, e);
        }
    }

    // 예약, 식당 아이디 추출
    private ReservationCreateEventDTOV2 parseMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(message, ReservationCreateEventDTOV2.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid message format: " + message, e);
        }
    }
}
