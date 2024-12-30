package com.qring.queue.presentation.v1.controller;

import com.qring.queue.application.v1.dto.QueueGetByIdResDTOv1;
import com.qring.queue.application.v1.dto.QueueGetTableResDTOv1;
import com.qring.queue.application.v1.dto.QueuePostResDTOv1;
import com.qring.queue.application.v1.dto.ResDTO;
import com.qring.queue.domain.model.QueueEntity;
import com.qring.queue.domain.model.constraint.QueueStatus;
import com.qring.queue.infrastructure.docs.QueueControllerSwagger;
import com.qring.queue.presentation.v1.req.PostQueueReqDTOv1;
import com.qring.queue.presentation.v1.req.PutQueueReqDTOv1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QueueControllerV1 implements QueueControllerSwagger {

    @PostMapping("/v1/queues")
    public ResponseEntity<ResDTO<QueuePostResDTOv1>> postBy(@RequestBody PostQueueReqDTOv1 dto) {

        // 더미데이터 -> 추후 삭제 ---------------------------
        QueueEntity dummyQueueEntity = QueueEntity.builder()
                .reservationId(57L)
                .sequence(1)
                .status(QueueStatus.valueOf("WAITING"))
                .build();
        // ----------------------------------------------

        return new ResponseEntity<>(
                ResDTO.<QueuePostResDTOv1>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("대기 등록에 성공했습니다.")
                        .data(QueuePostResDTOv1.of(dummyQueueEntity))
                        .build(),
                HttpStatus.CREATED
        );
    }

    // 추후 예약 서비스에서 메세지 큐로 받아온 인원 수 추가 예정입니다.
    @GetMapping("/v1/queues")
    public ResponseEntity<ResDTO<QueueGetTableResDTOv1>> getBy() {

        // 더미데이터 -> 추후 삭제 ---------------------------
        List<QueueEntity> dummyQueueList = List.of(
                QueueEntity.builder()
                        .reservationId(57L)
                        .sequence(1)
                        .status(QueueStatus.valueOf("WAITING"))
                        .build(),
                QueueEntity.builder()
                        .reservationId(58L)
                        .sequence(2)
                        .status(QueueStatus.valueOf("WAITING"))
                        .build(),
                QueueEntity.builder()
                        .reservationId(59L)
                        .sequence(3)
                        .status(QueueStatus.valueOf("WAITING"))
                        .build()
                );

        // ----------------------------------------------

        return new ResponseEntity<>(
                ResDTO.<QueueGetTableResDTOv1>builder()
                        .code(HttpStatus.OK.value())
                        .message("대기 조회에 성공했습니다.")
                        .data(QueueGetTableResDTOv1.of(dummyQueueList))
                        .build(),
                HttpStatus.OK
        );
    }


    @GetMapping("/v1/queues/{id}")
    public ResponseEntity<ResDTO<QueueGetByIdResDTOv1>> getBy(@PathVariable Long id) {

        // 더미데이터 -> 추후 삭제 ---------------------------
        QueueEntity dummyQueueEntity = QueueEntity.builder()
                .reservationId(57L)
                .sequence(1)
                .status(QueueStatus.valueOf("WAITING"))
                .build();
        // ----------------------------------------------

        return new ResponseEntity<>(
                ResDTO.<QueueGetByIdResDTOv1>builder()
                        .code(HttpStatus.OK.value())
                        .message("대기 조회에 성공했습니다.")
                        .data(QueueGetByIdResDTOv1.of(dummyQueueEntity))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/v1/queues/{id}")
    public ResponseEntity<ResDTO<Object>> putBy(@PathVariable Long id,
                                           @RequestBody PutQueueReqDTOv1 dto) {

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("대기 상태 수정에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/v1/queues/{id}")
    public ResponseEntity<ResDTO<Object>> cancelBy(@PathVariable Long id) {

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("대기 취소에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }
}
