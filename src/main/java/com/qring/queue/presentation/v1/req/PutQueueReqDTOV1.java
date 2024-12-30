package com.qring.queue.presentation.v1.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PutQueueReqDTOV1 {

    private Queue queue;

    @Getter
    public static class Queue {

        private String status;

    }
}
