package com.qring.queue.infrastructure.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationAndQueueEventDTOV2 {

    private Long reservationId;     // 예약 ID
    private Long restaurantId;      // 레스토랑 ID
    private String restaurantName;  // 레스토랑 이름
    private String restaurantTel;   // 레스토랑 전화번호
    private Long userId;            // 사용자 ID
    private String slackEmail;      // 사용자의 슬랙 이메일
    private int headCount;          // 예약 인원수
    private String username;        // 사용자 닉네임
    private int sequence;           // 대기 번호

    public static ReservationAndQueueEventDTOV2 from(ReservationCreationEventDTOV2 event, int sequence) {
        return ReservationAndQueueEventDTOV2.builder()
                .reservationId(event.getReservationInfo().getId())
                .restaurantId(event.getReservationInfo().getRestaurantId())
                .restaurantName(event.getRestaurantInfo().getRestaurantName())
                .restaurantTel(event.getRestaurantInfo().getRestaurantTel())
                .userId(event.getUserInfo().getUserId())
                .slackEmail(event.getUserInfo().getSlackEmail())
                .headCount(event.getReservationInfo().getHeadCount())
                .sequence(sequence)
                .build();
    }
}