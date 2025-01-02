package com.qring.queue.infrastructure.repository;

import com.qring.queue.domain.model.QueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueRepository extends JpaRepository<QueueEntity, Long> {
}
