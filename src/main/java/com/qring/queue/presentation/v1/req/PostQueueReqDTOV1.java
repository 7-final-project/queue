package com.qring.queue.presentation.v1.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostQueueReqDTOV1 {

        private Long reservationId;

        public static PostQueueReqDTOV1 of(Long reservationId) {
                PostQueueReqDTOV1 dto = new PostQueueReqDTOV1();
                dto.reservationId = reservationId;
                return dto;
        }
}
