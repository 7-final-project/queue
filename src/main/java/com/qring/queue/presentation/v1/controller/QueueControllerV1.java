package com.qring.queue.presentation.v1.controller;

import com.qring.queue.application.v1.res.QueueGetByIdResDTOV1;
import com.qring.queue.application.v1.res.QueueGetTableResDTOV1;
import com.qring.queue.application.v1.res.QueuePostResDTOV1;
import com.qring.queue.application.global.dto.ResDTO;
import com.qring.queue.domain.model.QueueEntity;
import com.qring.queue.domain.model.constraint.QueueStatus;
import com.qring.queue.infrastructure.docs.QueueControllerSwagger;
import com.qring.queue.presentation.v1.req.PostQueueReqDTOV1;
import com.qring.queue.presentation.v1.req.PutQueueReqDTOV1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/queues")
public class QueueControllerV1 implements QueueControllerSwagger {

    @PostMapping
    public ResponseEntity<ResDTO<QueuePostResDTOV1>> postBy(@RequestBody PostQueueReqDTOV1 dto) {

        // 더미데이터 -> 추후 삭제 ---------------------------
        QueueEntity dummyQueueEntity = QueueEntity.builder()
                .reservationId(57L)
                .sequence(1)
                .status(QueueStatus.valueOf("WAITING"))
                .build();
        // ----------------------------------------------

        return new ResponseEntity<>(
                ResDTO.<QueuePostResDTOV1>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("대기 등록에 성공했습니다.")
                        .data(QueuePostResDTOV1.of(dummyQueueEntity))
                        .build(),
                HttpStatus.CREATED
        );
    }

    // 추후 예약 서비스에서 메세지 큐로 받아온 인원 수 추가 예정입니다.
    @GetMapping
    public ResponseEntity<ResDTO<QueueGetTableResDTOV1>> getBy() {

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
                ResDTO.<QueueGetTableResDTOV1>builder()
                        .code(HttpStatus.OK.value())
                        .message("대기 조회에 성공했습니다.")
                        .data(QueueGetTableResDTOV1.of(dummyQueueList))
                        .build(),
                HttpStatus.OK
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResDTO<QueueGetByIdResDTOV1>> getBy(@PathVariable Long id) {

        // 더미데이터 -> 추후 삭제 ---------------------------
        QueueEntity dummyQueueEntity = QueueEntity.builder()
                .reservationId(57L)
                .sequence(1)
                .status(QueueStatus.valueOf("WAITING"))
                .build();
        // ----------------------------------------------

        return new ResponseEntity<>(
                ResDTO.<QueueGetByIdResDTOV1>builder()
                        .code(HttpStatus.OK.value())
                        .message("대기 조회에 성공했습니다.")
                        .data(QueueGetByIdResDTOV1.of(dummyQueueEntity))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResDTO<Object>> putBy(@PathVariable Long id,
                                           @RequestBody PutQueueReqDTOV1 dto) {

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("대기 상태 수정에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
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
