package com.qring.queue.application.res;

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
    public static class QueueInfo {
        private int sequence;
        private int teamsAhead;

        public static QueueInfo from(int sequence, int teamsAhead) {
            return QueueInfo.builder()
                    .sequence(sequence)
                    .teamsAhead(teamsAhead)
                    .build();
        }
    }
}
