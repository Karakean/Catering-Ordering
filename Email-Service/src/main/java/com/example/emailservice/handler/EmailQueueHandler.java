package com.example.emailservice.handler;

import com.catering.commons.dto.OrderEmailDto;
import com.example.emailservice.service.interfaces.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.emailservice.config.RabbitMqConfig.EMAIL_QUEUE_NAME;

@AllArgsConstructor
@Component
public class EmailQueueHandler {
    private final EmailSenderService emailSenderService;

    @RabbitListener(queues = EMAIL_QUEUE_NAME)
    public void handleMessage(Message message) {
        try {
            OrderEmailDto orderInfo = emailSenderService.extractContentFromMessage(message);
            System.out.println("Content extracted properly.");
            emailSenderService.sendEmail(orderInfo);
        } catch (IOException exception) {
            System.out.println("Failed to process message.");
        }
    }
}
