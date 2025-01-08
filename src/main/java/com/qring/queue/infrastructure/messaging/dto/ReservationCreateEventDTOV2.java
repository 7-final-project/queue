package com.qring.queue.infrastructure.messaging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationCreateEventDTOV2 {
    @JsonProperty("id")
    private Long reservationId;

    @JsonProperty("restaurantId")
    private Long restaurantId;
}