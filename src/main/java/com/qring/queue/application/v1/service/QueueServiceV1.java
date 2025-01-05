package com.qring.queue.application.v1.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qring.queue.application.v1.res.QueueInfoDTOV1;
import com.qring.queue.application.v1.res.QueuePostResDTOV1;
import com.qring.queue.domain.model.QueueEntity;
import com.qring.queue.infrastructure.repository.QueueRepository;
import com.qring.queue.presentation.v1.req.PostQueueReqDTOV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueServiceV1 {

    private final QueueRepository queueRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String WAITING_LIST_KEY = "waiting_list";

    // 대기 생성
    @Transactional
    public QueuePostResDTOV1 postBy(PostQueueReqDTOV1 dto) {

        QueueInfoDTOV1 queueInfo = getQueueInfoBy(String.valueOf(dto.getReservationId()));

        QueueEntity queueEntity = QueueEntity.builder()
                .reservationId(dto.getReservationId())
                .build();

        queueEntity = queueRepository.save(queueEntity);



        return QueuePostResDTOV1.of(queueEntity, queueInfo);
    }

    @KafkaListener(topics = "reservation-create-event-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void handleMessage(String message) {
        try {
            // 메시지에서 예약 ID를 추출하고 DTO로 변환
            PostQueueReqDTOV1 dto = extractReservationIdFrom(message);

            // 대기열에 등록 및 정보 반환
            QueuePostResDTOV1 response = postBy(dto);

            // 결과 로깅 또는 추가 처리
            System.out.println("Queue registered: " + response.getQueueInfo().getSequence());
        } catch (Exception e) {
            log.error("메세지 추출에 실패하였습니다. : {}", message, e);
        }
    }

    // 예약 ID를 추출하여 DTO로 변환
    private PostQueueReqDTOV1 extractReservationIdFrom(String message) {
        Long reservationId = parseReservationIdFrom(message);

        return PostQueueReqDTOV1.of(reservationId);
    }

    // 예약 아이디 추출
    private Long parseReservationIdFrom(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);
            return jsonNode.get("reservationId").asLong();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid message format: " + message, e);
        }
    }

    // 사용자 대기 순서와 총 대기 인원 반환
    public QueueInfoDTOV1 getQueueInfoBy(String reservationId) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

        // 사용자 대기 순서 조회
        int rank = getSeqBy(zSetOps, WAITING_LIST_KEY, reservationId);

        // 대기열에 사용자가 없을 경우 등록
        if (rank == -1) {
            addWaitingListBy(reservationId);
            rank = getSeqBy(zSetOps, WAITING_LIST_KEY, reservationId);
        }

        // 총 대기 인원 조회
        int totalWaitingCount = Optional.ofNullable(zSetOps.zCard(WAITING_LIST_KEY))
                .orElse(0L) // 기본값 설정
                .intValue();

        // 대기 순서는 Redis의 rank가 0부터 시작하므로 1을 더해 반환
        return QueueInfoDTOV1.of(rank + 1, totalWaitingCount);
    }

    // 대기열에 등록
    private void addWaitingListBy(String reservationId) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        long score = System.currentTimeMillis(); // 대기 순서를 보장하기 위해 timestamp 사용
        zSetOps.add(WAITING_LIST_KEY, reservationId, score);
    }

    // 사용자 순서 조회 반환
    private int getSeqBy(ZSetOperations<String, Object> zSetOps, String key, String value) {
        return Optional.ofNullable(zSetOps.rank(key, value))
                .map(Long::intValue)
                .orElse(-1);
    }
}
