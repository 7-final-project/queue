package com.qring.queue.infrastructure.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QueueAlarmEventDTO {
    private Long reservationId;
}
