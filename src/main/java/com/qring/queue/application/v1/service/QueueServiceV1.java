package com.qring.queue.application.v1.service;

import com.qring.queue.application.v1.res.QueuePostResDTOV1;
import com.qring.queue.infrastructure.repository.QueueRepository;
import com.qring.queue.presentation.v1.req.PostQueueReqDTOV1;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueServiceV1 {

    private final QueueRepository queueRepository;

    @KafkaListener(groupId = "", topics = "")
    public QueuePostResDTOV1 postBy(PostQueueReqDTOV1 dto) {
        return null;
    }
}
