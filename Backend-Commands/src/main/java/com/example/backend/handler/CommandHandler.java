package com.example.backend.handler;

import com.catering.commons.domain.enums.EventType;
import com.catering.commons.domain.event.AbstractEvent;
import com.catering.commons.domain.event.CateringCreatedEvent;
import com.catering.commons.domain.event.OrderCreatedEvent;
import com.example.backend.domain.entity.Catering;
import com.example.backend.domain.entity.Order;
import com.example.backend.mapper.CateringMapper;
import com.example.backend.mapper.OrderMapper;
import com.example.backend.command.create.CreateCateringCommand;
import com.example.backend.command.create.CreateOrderCommand;
import com.example.backend.service.interfaces.CateringService;
import com.example.backend.service.interfaces.NotificationService;
import com.example.backend.service.interfaces.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

import static com.example.backend.config.QueuesConfig.COMMAND_QUEUE_NAME;
import static com.example.backend.config.QueuesConfig.EVENT_QUEUE_NAME;

@AllArgsConstructor
@Component
public class CommandHandler {
    private final OrderService orderService;
    private final CateringService cateringService;
    private final NotificationService notificationService;
    private final OrderMapper orderMapper;
    private final CateringMapper cateringMapper;

    @RabbitListener(queues = COMMAND_QUEUE_NAME)
    public void handleCommands(Message message) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        String commandType = (String) headers.get("command_type");
        try {
            switch (commandType) {
                case "create_order" -> handleCreateOrderCommand(message.getBody());
                case "create_catering" -> handleCreateCateringCommand(message.getBody());
                default -> System.out.println("Unknown command type or command_type header does not exist.");
            }
        } catch (Exception exception) {
            System.out.println("Processing command failed.");
        }
    }

    private void handleCreateOrderCommand(byte[] messageBody) throws IOException {
        CreateOrderCommand createCommand = new ObjectMapper().readValue(messageBody, CreateOrderCommand.class);
        Order createdOrder = orderService.create(orderMapper.mapCommandToEntity(createCommand));
        OrderCreatedEvent event = orderMapper.mapEntityToEvent(createdOrder);
        notificationService.sendEvent(event, EventType.CREATE_ORDER);
        System.out.println(createCommand.getPurchaserEmail() + " just made an order.");
        notificationService.sendEmail(createdOrder);
    }

    private void handleCreateCateringCommand(byte[] messageBody) throws IOException {
        CreateCateringCommand createCommand = new ObjectMapper().readValue(messageBody, CreateCateringCommand.class);
        Catering createdCatering = cateringService.create(cateringMapper.mapCommandToEntity(createCommand));
        CateringCreatedEvent event = cateringMapper.mapEntityToEvent(createdCatering);
        notificationService.sendEvent(event, EventType.CREATE_CATERING);
        System.out.println("Catering " + createdCatering.getName() + " was just created.");
    }
}
