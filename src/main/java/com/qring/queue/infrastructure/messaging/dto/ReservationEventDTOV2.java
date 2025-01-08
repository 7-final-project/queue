package com.qring.queue.infrastructure.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEventDTOV2 {

    private Long reservationId;
    private Long restaurantId;
    private String restaurantName;
    private String restaurantTel;
    private Long userId;
    private String slackEmail;
    private int headCount;

    public static ReservationEventDTOV2 from(ReservationAndQueueEventDTOV2 dto) {
        return ReservationEventDTOV2.builder()
                .reservationId(dto.getReservationId())
                .restaurantId(dto.getRestaurantId())
                .restaurantName(dto.getRestaurantName())
                .restaurantTel(dto.getRestaurantTel())
                .userId(dto.getUserId())
                .slackEmail(dto.getSlackEmail())
                .headCount(dto.getHeadCount())
                .build();
    }
}
