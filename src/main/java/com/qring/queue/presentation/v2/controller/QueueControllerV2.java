package com.qring.queue.presentation.v2.controller;

import com.qring.queue.application.global.dto.ResDTO;
import com.qring.queue.application.v2.res.QueueGetResDTOV2;
import com.qring.queue.application.v2.service.QueueServiceV2;
import com.qring.queue.infrastructure.docs.QueueControllerSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/queues")
public class QueueControllerV2 implements QueueControllerSwagger {

    private final QueueServiceV2 queueServiceV2;

    @GetMapping
    public ResponseEntity<ResDTO<QueueGetResDTOV2>> getBy(@RequestParam Long restaurantId,
                                                        @RequestParam Long reservationId) {

        return new ResponseEntity<>(
                ResDTO.<QueueGetResDTOV2>builder()
                        .code(HttpStatus.OK.value())
                        .message("대기 조회에 성공했습니다.")
                        .data(queueServiceV2.getInfoByRestaurantIdAndReservationId(restaurantId, reservationId))
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping
    public ResponseEntity<ResDTO<Object>> removeBy(@RequestParam Long restaurantId,
                                                   @RequestParam Long reservationId) {

        queueServiceV2.removeQueueByRestaurantIdAndReservationId(restaurantId, reservationId);

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("대기 삭제에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }
}
