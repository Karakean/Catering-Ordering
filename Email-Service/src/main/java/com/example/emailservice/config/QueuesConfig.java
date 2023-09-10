package com.example.emailservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

public class QueuesConfig {
    public static final String EMAIL_QUEUE_NAME = "email_queue";
    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE_NAME);
    }
}
