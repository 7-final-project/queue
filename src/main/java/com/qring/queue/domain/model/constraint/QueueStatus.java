package com.qring.queue.domain.model.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QueueStatus {

    SEATED("SEATED"),
    WAITING("WAITING"),
    CANCELLED("CANCELLED");

    private final String value;
}
