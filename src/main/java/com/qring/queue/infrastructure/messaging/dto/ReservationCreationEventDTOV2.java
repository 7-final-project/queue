package com.qring.queue.infrastructure.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreationEventDTOV2 {

    private UserInfo userInfo;
    private RestaurantInfo restaurantInfo;
    private ReservationInfo reservationInfo;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {

        private Long userId;
        private String slackEmail;
        private String username;

        public static UserInfo from(Long userId, String slackEmail, String username) {
            return UserInfo.builder()
                    .userId(userId)
                    .slackEmail(slackEmail)
                    .username(username)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RestaurantInfo {

        private String restaurantName;
        private String restaurantTel;

        public static RestaurantInfo from(String restaurantName, String restaurantTel) {
            return RestaurantInfo.builder()
                    .restaurantName(restaurantName)
                    .restaurantTel(restaurantTel)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationInfo {

        private Long id;
        private Long restaurantId;
        private int headCount;

        public static ReservationInfo from(Long id, Long restaurantId, int headCount) {
            return ReservationInfo.builder()
                    .id(id)
                    .restaurantId(restaurantId)
                    .headCount(headCount)
                    .build();
        }
    }
}
