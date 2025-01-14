package com.qring.queue.application.global.exception;

import lombok.Getter;

@Getter
public class QueueException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public QueueException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

}