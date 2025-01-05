package com.qring.queue.application.v1.res;

import com.qring.queue.domain.model.QueueEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueuePostResDTOV1 {

    private Queue queue;
    private QueueInfoDTO queueInfo;

    public static QueuePostResDTOV1 of(QueueEntity queueEntity, QueueInfoDTO queueInfo) {
        return QueuePostResDTOV1.builder()
                .queue(Queue.from(queueEntity))
                .queueInfo(QueueInfoDTO.of(queueInfo.getSequence(), queueInfo.getTotal()))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Queue {

        private String status;
        private Long number;

        public static Queue from(QueueEntity queueEntity) {
            return Queue.builder()
                    .number(queueEntity.getNumber())
                    .status(queueEntity.getStatus().getValue())
                    .build();
        }
    }
}
