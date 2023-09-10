package com.example.backend.handler;

import com.catering.commons.domain.enums.EventType;
import com.catering.commons.domain.event.CateringCreatedEvent;
import com.catering.commons.domain.event.OrderCreatedEvent;
import com.example.backend.domain.entity.Catering;
import com.example.backend.domain.entity.Order;
import com.example.backend.mapper.CateringMapper;
import com.example.backend.mapper.OrderMapper;
import com.example.backend.service.interfaces.CateringService;
import com.example.backend.service.interfaces.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.example.backend.config.RabbitMqConfig.EVENT_QUEUE_NAME;

@AllArgsConstructor
@Component
public class EventHandler {
    private final OrderService orderService;
    private final CateringService cateringService;
    private final OrderMapper orderMapper;
    private final CateringMapper cateringMapper;

//    @RabbitListener(queues = "event_queue")
//    public void handleEventMessage(Message message) {
//        try {
//            Map<String, Object> headers = message.getMessageProperties().getHeaders();
//            String eventTypeString = (String) headers.get("event_type");
//            EventType eventType = EventType.fromDisplayName(eventTypeString);
//            switch (eventType) {
//                case CREATE_ORDER -> handleOrderCreatedEvent(message.getBody());
//                case CREATE_CATERING -> handleCateringCreatedEvent(message.getBody());
//                default -> System.out.println("Unknown event type.");
//            }
//        } catch (Exception exception) {
//            System.out.println("Event processing failed.\n");
//            exception.printStackTrace();
//        }
//    }
@RabbitListener(queues = EVENT_QUEUE_NAME)
public void handleEventMessage(Message message) {
    try {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        String eventTypeString = (String) headers.get("event_type");
        EventType eventType = EventType.fromDisplayName(eventTypeString);
        switch (eventType) {
            case CREATE_ORDER -> handleOrderCreatedEvent(new String(message.getBody()));
            case CREATE_CATERING -> handleCateringCreatedEvent(new String(message.getBody()));
            default -> System.out.println("Unknown event type.");
        }
    } catch (Exception exception) {
        System.out.println("Event processing failed.\n");
        exception.printStackTrace();
    }
}

    private void handleOrderCreatedEvent(String messageBody) throws IOException {
        OrderCreatedEvent event = new ObjectMapper().readValue(messageBody, OrderCreatedEvent.class);
        Order order = orderMapper.mapEventToEntity(event);
        orderService.create(order);
        System.out.printf("Order with id: %d saved.\n", order.getId());
    }

    private void handleCateringCreatedEvent(String messageBody) throws IOException {
        CateringCreatedEvent event = new ObjectMapper().readValue(messageBody, CateringCreatedEvent.class);
        Catering catering = cateringMapper.mapEventToEntity(event);
        cateringService.create(catering);
        System.out.printf("Catering with id: %d and name: %s saved.\n", catering.getId(), catering.getName());
    }
}
