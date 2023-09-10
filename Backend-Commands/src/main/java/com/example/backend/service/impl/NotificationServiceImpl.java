package com.example.backend.service.impl;

import com.catering.commons.domain.enums.EventType;
import com.catering.commons.domain.event.AbstractEvent;
import com.example.backend.domain.entity.Order;
import com.example.backend.mapper.OrderMapper;
import com.example.backend.service.interfaces.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.stereotype.Service;

import static com.example.backend.config.QueuesConfig.EMAIL_QUEUE_NAME;
import static com.example.backend.config.QueuesConfig.EVENT_QUEUE_NAME;

@AllArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    private final RabbitTemplate rabbitTemplate;
    private final OrderMapper orderMapper;

//    public void sendEvent(AbstractEvent event, EventType eventType) {
//        MessageProperties messageEventProperties = new MessageProperties();
//        messageEventProperties.setHeader("event_type", "CREATE_CATERING"); //TODO eventType.getDisplayName());
//        byte[] eventSerialized = SerializationUtils.serialize(event);
//        Message eventMessage = MessageBuilder.withBody(eventSerialized)
//                .andProperties(messageEventProperties)
//                .build();
//        rabbitTemplate.convertAndSend(EVENT_QUEUE_NAME, eventMessage);
//    }

    public void sendEvent(AbstractEvent event, EventType eventType) throws JsonProcessingException {
        MessageProperties messageEventProperties = new MessageProperties();
        messageEventProperties.setHeader("event_type", eventType.getDisplayName());
        String eventJson = new ObjectMapper().writeValueAsString(event);
        Message eventMessage = MessageBuilder.withBody(eventJson.getBytes())
                .andProperties(messageEventProperties)
                .build();
        rabbitTemplate.convertAndSend(EVENT_QUEUE_NAME, eventMessage);
    }

    public void sendEmail(Order createdOrder) throws JsonProcessingException {
        String jsonMessage = new ObjectMapper().writeValueAsString(orderMapper.mapEntityToEmailDto(createdOrder));
        rabbitTemplate.convertAndSend(EMAIL_QUEUE_NAME, jsonMessage.getBytes());
    }
}
