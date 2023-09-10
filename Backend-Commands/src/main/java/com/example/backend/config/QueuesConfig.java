package com.example.backend.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueuesConfig {
    public static final String COMMAND_QUEUE_NAME = "command_queue";
    public static final String COMMAND_RESPONSE_QUEUE_NAME = "command_queue";
    public static final String EVENT_QUEUE_NAME = "event_queue";
    public static final String EMAIL_QUEUE_NAME = "email_queue";

    @Bean
    public Queue commandQueue() {
        return new Queue("command_queue");
    }

    @Bean
    public Queue commandResponseQueue() {
        return new Queue(COMMAND_RESPONSE_QUEUE_NAME);
    }

    @Bean
    public Queue eventQueue() {
        return new Queue(EVENT_QUEUE_NAME);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE_NAME);
    }
}