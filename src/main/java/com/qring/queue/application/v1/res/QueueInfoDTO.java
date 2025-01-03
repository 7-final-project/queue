package com.qring.queue.application.v1.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueInfoDTO {
    private int sequence;
    private int total;

    public static QueueInfoDTO of(int sequence, int total) {
        return QueueInfoDTO.builder()
                .sequence(sequence)
                .total(total)
                .build();
    }
}
