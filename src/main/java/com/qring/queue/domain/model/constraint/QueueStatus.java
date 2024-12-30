package com.qring.queue.domain.model.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QueueStatus {

    SEATED(Status.SEATED),
    WAITING(Status.WAITING),
    CANCELLED(Status.CANCELLED);

    private final String status;

    public static class Status {
        public static final String SEATED = "입장";
        public static final String WAITING = "대기";
        public static final String CANCELLED = "취소";
    }
}
