package com.example.backend.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String EVENT_QUEUE_NAME = "event_queue";
    public static final String QUERY_QUEUE_NAME = "query_queue";

    //public static final String QUERY_RESPONSE_QUEUE_NAME = "query_response_queue";

    @Bean
    public Queue eventQueue() {
        return new Queue(EVENT_QUEUE_NAME);
    }

    @Bean
    public Queue queryQueue() {
        return new Queue(QUERY_QUEUE_NAME);
    }

//    @Bean
//    public Queue queryResponseQueue() {
//        return new Queue(QUERY_RESPONSE_QUEUE_NAME);
//    }
}