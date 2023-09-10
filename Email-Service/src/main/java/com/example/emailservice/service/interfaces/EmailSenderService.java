package com.example.emailservice.service.interfaces;

import com.catering.commons.dto.OrderEmailDto;
import org.springframework.amqp.core.Message;

import java.io.IOException;

public interface EmailSenderService {
    void sendEmail(OrderEmailDto orderInfo);
    OrderEmailDto extractContentFromMessage(Message message) throws IOException;
}
