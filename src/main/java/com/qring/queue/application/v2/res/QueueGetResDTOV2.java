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

    private QueueInfo queueInfo;

    public static QueueGetResDTOV2 of(QueueInfo queueInfo) {
        return QueueGetResDTOV2.builder()
                .queueInfo(queueInfo)
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class QueueInfo {
        private int sequence;
        private int total;

        public static QueueInfo from(int sequence, int total) {
            return QueueInfo.builder()
                    .sequence(sequence)
                    .total(total)
                    .build();
        }
    }



}
