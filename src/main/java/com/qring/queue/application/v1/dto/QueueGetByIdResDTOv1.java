package com.qring.queue.application.v1.dto;

import com.qring.queue.domain.model.QueueEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueGetByIdResDTOv1 {

    private QueuePostResDTOv1.Queue queue;

    public static QueuePostResDTOv1 of(QueueEntity queueEntity) {
        return QueuePostResDTOv1.builder()
                .queue(QueuePostResDTOv1.Queue.from(queueEntity))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Queue {

        private int sequence;
        private String status;

        public static QueuePostResDTOv1.Queue from(QueueEntity queueEntity) {
            return QueuePostResDTOv1.Queue.builder()
                    .sequence(queueEntity.getSequence())
                    .status(queueEntity.getStatus().toString())
                    .build();
        }
    }
}
