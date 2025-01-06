package com.qring.queue.application.v1.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueInfoDTOV1 {
    private int sequence;
    private int total;

    public static QueueInfoDTOV1 of(int sequence, int total) {
        return QueueInfoDTOV1.builder()
                .sequence(sequence)
                .total(total)
                .build();
    }
}
