package com.example.backend.handler;

import com.catering.commons.domain.enums.EventType;
import com.catering.commons.domain.event.CateringCreatedEvent;
import com.catering.commons.domain.event.OrderCreatedEvent;
import com.catering.commons.exception.CateringNotFoundException;
import com.catering.commons.exception.DatabaseUnavailableException;
import com.example.backend.command.create.CreateCateringCommand;
import com.example.backend.command.create.CreateOrderCommand;
import com.example.backend.domain.entity.Catering;
import com.example.backend.domain.entity.Order;
import com.example.backend.exception.InvalidCorrelationIdException;
import com.example.backend.mapper.CateringMapper;
import com.example.backend.mapper.OrderMapper;
import com.example.backend.service.interfaces.CateringService;
import com.example.backend.service.interfaces.NotificationService;
import com.example.backend.service.interfaces.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.CannotCreateTransactionException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.backend.config.RabbitMqConfig.COMMAND_QUEUE_NAME;

@Component
@EnableScheduling
public class CommandHandler {
    private final OrderService orderService;
    private final CateringService cateringService;
    private final NotificationService notificationService;
    private final OrderMapper orderMapper;
    private final CateringMapper cateringMapper;
    private final AtomicBoolean isDatabaseDown;

    public CommandHandler(OrderService orderService,
                          CateringService cateringService,
                          NotificationService notificationService,
                          OrderMapper orderMapper,
                          CateringMapper cateringMapper) {
        this.orderService = orderService;
        this.cateringService = cateringService;
        this.notificationService = notificationService;
        this.orderMapper = orderMapper;
        this.cateringMapper = cateringMapper;
        this.isDatabaseDown = new AtomicBoolean(false);
    }

    @RabbitListener(queues = COMMAND_QUEUE_NAME)
    public void handleCommands(Message message) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        String commandType = (String) headers.get("command_type");
        String userId = (String) headers.get("user_id");
        try {
            if (userId == null) {
                throw new InvalidCorrelationIdException("Wrong correlation id!");
            } else if (isDatabaseDown.get()) {
                throw new DatabaseUnavailableException("Database is still down!");
            }
            switch (commandType) {
                case "create_order" -> handleCreateOrderCommand(message.getBody(), userId);
                case "create_catering" -> handleCreateCateringCommand(message.getBody(), userId);
                default -> System.out.println("Unknown command type or command_type header does not exist.");
            }
        } catch (CannotCreateTransactionException | DatabaseUnavailableException databaseException) {
            System.out.println("Database issues. The connection will be checked every 30s.");
            notificationService.sendResponse("There was a problem with processing your request. " +
                    "Please try again later.", userId);
            isDatabaseDown.set(true);
        } catch (Exception exception) {
            System.out.println("Processing command failed.");
            exception.printStackTrace();
        }
    }

    private void handleCreateOrderCommand(byte[] messageBody, String userId) throws IOException, CateringNotFoundException, AmqpException {
        CreateOrderCommand createCommand = new ObjectMapper().readValue(messageBody, CreateOrderCommand.class);
        Order createdOrder = orderService.create(orderMapper.mapCommandToEntity(createCommand));
        OrderCreatedEvent event = orderMapper.mapEntityToEvent(createdOrder);
        notificationService.sendEvent(event, EventType.CREATE_ORDER);
        System.out.println(createCommand.getPurchaserEmail() + " just made an order.");
        notificationService.sendResponse("Your order was processed successfully. " +
                "Soon you'll receive an email with order details.", userId);
        notificationService.sendEmail(createdOrder);
    }

    private void handleCreateCateringCommand(byte[] messageBody, String userId) throws IOException, AmqpException {
        CreateCateringCommand createCommand = new ObjectMapper().readValue(messageBody, CreateCateringCommand.class);
        Catering createdCatering = cateringService.create(cateringMapper.mapCommandToEntity(createCommand));
        CateringCreatedEvent event = cateringMapper.mapEntityToEvent(createdCatering);
        notificationService.sendEvent(event, EventType.CREATE_CATERING);
        System.out.println("Catering " + createdCatering.getName() + " was just created.");
        notificationService.sendResponse("Catering added successfully.", userId);
    }

    @Scheduled(fixedDelay = 30000)
    protected void checkDatabaseHealth() {
        if (!isDatabaseDown.get()) {
            return;
        }
        try {
            cateringService.findById(1L);
            System.out.println("Database is working again.");
            isDatabaseDown.set(false);
        } catch (Exception e) {
            System.out.println("Database is down. Next check in 30 seconds.");
        }
    }
}
