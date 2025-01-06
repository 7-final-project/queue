package com.qring.queue.infrastructure.messaging.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationCreateEventDTO {
    private Long reservationId;
    private Long restaurantId;
}
