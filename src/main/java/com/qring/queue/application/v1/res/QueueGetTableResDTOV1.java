package com.qring.queue.application.v1.res;

import com.qring.queue.domain.model.QueueEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueGetTableResDTOV1 {

    private List<Queue> queueList;

    public static QueueGetTableResDTOV1 of(List<QueueEntity> queueEntityList) {
        return QueueGetTableResDTOV1.builder()
                .queueList(queueEntityList.stream()
                        .map(Queue::from)
                        .collect(Collectors.toList()))
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

