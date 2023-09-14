package com.example.backend.service.interfaces;

import com.catering.commons.domain.enums.EventType;
import com.catering.commons.domain.event.AbstractEvent;
import com.example.backend.domain.entity.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.AmqpException;

public interface NotificationService {
    void sendEvent(AbstractEvent event, EventType eventType) throws JsonProcessingException, AmqpException;
    void sendEmail(Order createdOrder) throws JsonProcessingException, AmqpException;
    void sendResponse(String messageContent, String userId) throws AmqpException;
}
