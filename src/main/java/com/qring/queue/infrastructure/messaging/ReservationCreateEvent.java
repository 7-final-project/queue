package com.qring.queue.infrastructure.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationCreateEvent {
    private Long reservationId;
    private Long restaurantId;
}
