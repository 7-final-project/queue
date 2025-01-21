package com.qring.queue.infrastructure.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qring.queue.application.global.exception.ErrorCode;
import com.qring.queue.application.global.exception.QueueException;
import com.qring.queue.application.v2.message.kafka.KafkaMessageProducerV2;
import com.qring.queue.application.v2.res.QueueGetResDTOV2;
import com.qring.queue.application.v2.service.QueueServiceV2;
import com.qring.queue.infrastructure.messaging.dto.CreateReservationMessageDTOV2;
import com.qring.queue.infrastructure.messaging.dto.UpdateReservationMessageDTOV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "QueueService - KafkaMessageConsumerV2 Log")
public class KafkaMessageConsumerV2 {

    // -----
    // NOTE : 다중 파티션을 적용한 카프카 컨슈머입니다.
    // -----

    private final QueueServiceV2 queueServiceV2;
    private final KafkaMessageProducerV2 kafkaMessageProducerV2;
    private final ObjectMapper objectMapper;

    // -----
    /*
        NOTE
          1. 단일 파티션          : v1
          2. 단일 파티션 + 배치처리 : v2
          3. 다중 파티션          : v3
          4. 다중 파티션 + 배치처리 : v4
          5. 다중 토픽           : v5
    */
    // -----
    @KafkaListener(topics = "${spring.kafka.topic.reservation-create-event.v1}", groupId = "${spring.kafka.consumer.group-id}")
    public void extractReservationInfoBy(String message) {
        long startTime = System.currentTimeMillis();
        Long reservationId = 0L;

        try {
            CreateReservationMessageDTOV2 parsedMessage = objectMapper.readValue(message, CreateReservationMessageDTOV2.class);

            reservationId = parsedMessage.getReservation().getId();

            // NOTE: 대기열에 등록
            queueServiceV2.enrollWaitingListByEvent(parsedMessage);

            QueueGetResDTOV2.QueueInfo dto = queueServiceV2.getBy(
                            parsedMessage.getReservation().getRestaurant().getId(),
                            parsedMessage.getReservation().getId())
                    .getQueueInfo();

            CreateReservationMessageDTOV2.Queue queue = CreateReservationMessageDTOV2.Queue.from(dto.getSequence());

            // NOTE: 새로운 대기 정보를 포함한 DTO 생성
            parsedMessage = new CreateReservationMessageDTOV2(
                    parsedMessage.getUser(),
                    parsedMessage.getReservation(),
                    queue
            );

            // NOTE: 메시지 서비스로 전송
            kafkaMessageProducerV2.publishReservationAndQueueEvent(parsedMessage);

            long endTime = System.currentTimeMillis();
            log.info("메세지 처리 속도 : {} ms", endTime - startTime);
        } catch (Exception e) {

            log.error("대기열 생성 실패 : {}", message, e);

            kafkaMessageProducerV2.publishQueueCreateFailEvent(reservationId);

            throw new QueueException(ErrorCode.BAD_REQUEST_ERROR, "대기열 생성에 실패하였습니다.");
        }
    }

    // NOTE: 예약 입장 or 취소 이벤트 소비
    @KafkaListener(topics = "${spring.kafka.topic.reservation-update-event}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleReservationCancellation(String message) {
        Long reservationId = 0L;
        try {
            // NOTE: 메시지에서 예약 ID와 식당 ID 추출
            UpdateReservationMessageDTOV2 event = parseMessage(message);
            reservationId = event.getReservation().getId();
            queueServiceV2.deleteBy(event.getReservation().getRestaurant().getId(), event.getReservation().getId());
        } catch (Exception e) {
            log.error("메시지 추출 실패 : {}", message, e);

            kafkaMessageProducerV2.publishQueueDeleteFailEvent(reservationId);

            throw new QueueException(ErrorCode.BAD_REQUEST_ERROR, "대기열 삭제에 실패하였습니다.");
        }
    }

    // NOTE: 예약, 식당 아이디 추출
    private UpdateReservationMessageDTOV2 parseMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(message, UpdateReservationMessageDTOV2.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid message format: " + message, e);
        }
    }
}