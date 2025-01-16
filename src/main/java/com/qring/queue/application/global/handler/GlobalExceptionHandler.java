package com.qring.queue.application.global.handler;

import com.qring.queue.application.global.dto.ResDTO;
import com.qring.queue.application.global.exception.QueueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({QueueException.class})
    public ResponseEntity<ResDTO<Object>> authExceptionHandler(QueueException ex) {
        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(ex.getErrorCode().getCode())
                        .message(ex.getMessage())
                        .build(),
                ex.getErrorCode().getHttpStatus()
        );
    }
}
