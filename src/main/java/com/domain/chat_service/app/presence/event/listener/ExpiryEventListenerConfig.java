package com.domain.chat_service.app.presence.event.listener;

import com.domain.chat_service.app.presence.services.PresenceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class ExpiryEventListenerConfig {
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            MessageListener expiryListener
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        container.addMessageListener(expiryListener, new PatternTopic("__keyevent@*__:expired"));
        return container;
    }

    @Bean
    public MessageListener expiredKeyListener(PresenceService presenceService) {
        return (message, pattern) -> {
            String expiredKey = message.toString();

            if (expiredKey.startsWith("socket_user:")) {
                String sessionId = expiredKey.substring("socket_user:".length());
                presenceService.handleSessionExpiry(sessionId);
            }
        };
    }
}
