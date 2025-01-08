package com.qring.queue.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

@Configuration
@RequiredArgsConstructor
public class RedisListenerConfig {
    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(MessageListener redisKeyspaceListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        // Keyspace Notifications 채널 구독: zadd (추가) 및 zrem (삭제) 이벤트
        Topic zaddTopic = new PatternTopic("__keyevent@*__:zadd");
        Topic zremTopic = new PatternTopic("__keyevent@*__:zrem");

        container.addMessageListener(redisKeyspaceListener, zaddTopic);
        container.addMessageListener(redisKeyspaceListener, zremTopic);

        return container;
    }
}
