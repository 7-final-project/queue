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
public class QueuePostResDTOv1 {

    private Queue queue;

    public static QueuePostResDTOv1 of(QueueEntity queueEntity) {
        return QueuePostResDTOv1.builder()
                .queue(Queue.from(queueEntity))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Queue {

        private int sequence;
        private String status;

        public static Queue from(QueueEntity queueEntity) {
            return Queue.builder()
                    .sequence(queueEntity.getSequence())
                    .status(queueEntity.getStatus().toString())
                    .build();
        }
    }
}