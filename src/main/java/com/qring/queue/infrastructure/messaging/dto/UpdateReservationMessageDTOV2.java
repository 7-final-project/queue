package com.qring.queue.infrastructure.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReservationMessageDTOV2 {

    private Reservation reservation;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reservation {

        private Long id;
        private Restaurant restaurant;

        public static Reservation from(Long id, Restaurant restaurant) {
            return Reservation.builder()
                    .id(id)
                    .restaurant(restaurant)
                    .build();
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Restaurant {

            private Long id;

            public static Restaurant from(Long id) {
                return Restaurant.builder()
                        .id(id)
                        .build();
            }
        }
    }
}