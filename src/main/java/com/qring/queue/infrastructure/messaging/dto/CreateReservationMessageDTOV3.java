package com.qring.queue.infrastructure.messaging.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateReservationMessageDTOV3 {

    private Long restaurantId;
    private Long reservationId;

}
