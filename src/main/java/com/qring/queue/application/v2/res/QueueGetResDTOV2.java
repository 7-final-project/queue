package com.qring.queue.application.v2.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueGetResDTOV2 {
    private int sequence;
    private int total;

    public static QueueGetResDTOV2 of(int sequence, int total) {
        return QueueGetResDTOV2.builder()
                .sequence(sequence)
                .total(total)
                .build();
    }
}
