package com.qring.queue.application.global.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends QueueException {
    public BadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST_ERROR, message);
    }
}
