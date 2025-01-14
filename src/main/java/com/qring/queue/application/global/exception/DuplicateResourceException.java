package com.qring.queue.application.global.exception;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends QueueException {
    public DuplicateResourceException(String message) {
        super(ErrorCode.DUPLICATE_ERROR, message);
    }
}