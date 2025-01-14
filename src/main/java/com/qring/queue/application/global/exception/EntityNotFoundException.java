package com.qring.queue.application.global.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends QueueException {
    public EntityNotFoundException(String message) {
        super(ErrorCode.NOT_FOUND_ERROR, message);
    }
}