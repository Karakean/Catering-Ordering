package com.example.emailservice.service.impl;

import com.catering.commons.dto.OrderEmailDto;
import com.example.emailservice.service.interfaces.CalculationService;
import com.example.emailservice.service.interfaces.EmailSenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.text.DecimalFormat;

@AllArgsConstructor
@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    private final CalculationService calculationService;
    private final JavaMailSender mailSender;

    private String createMailContentFromOrderInfo(OrderEmailDto orderInfo) {
        StringBuilder orderedCateringsTextBuilder = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.##");
        orderInfo.getOrderPositions().forEach
                (catering -> orderedCateringsTextBuilder.append(String.format(
                        """
                            Catering: %s
                            Quantity: %d
                            Calories (per one catering): %d
                            Price per month (per one catering): %s
                            Price per month (combined per catering type): %s
                        """,
                        catering.getCateringName(),
                        catering.getQuantity(),
                        catering.getCalories(),
                        df.format(catering.getPricePerMonth()),
                        df.format(calculationService.calculatePricePerMonthPerOrderPosition(catering))))
                        .append("\n"));
        String orderedCateringsText = orderedCateringsTextBuilder.toString();
        return  "Hello " + orderInfo.getPurchaserName() +
                "!\nThanks for ordering our caterings. " +
                "Here's quick summary of your order:" +
                "\nOrdered caterings:\n\n" +
                orderedCateringsText +
                "Email: " + orderInfo.getPurchaserEmail() +
                "\nName: " + orderInfo.getPurchaserName() +
                "\nSurname: " + orderInfo.getPurchaserSurname() +
                "\nDelivery address: " + orderInfo.getAddress() +
                "\nPreferred delivery time: " + orderInfo.getPreferredDeliveryTime() +
                "\nPrice per month (combined): " + df.format(calculationService.calculatePricePerMonthPerOrder(orderInfo));
    }

    @Override
    public void sendEmail(OrderEmailDto orderInfo) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("mn.dev.184865@gmail.com");
        mail.setTo(orderInfo.getPurchaserEmail());
        mail.setSubject("Catering - Order No. " + orderInfo.getOrderId());
        mail.setText(createMailContentFromOrderInfo(orderInfo));
        System.out.println(createMailContentFromOrderInfo(orderInfo));
        mailSender.send(mail);
    }

    @Override
    public OrderEmailDto extractContentFromMessage(Message message) throws IOException {
        byte[] messageBody = message.getBody();
        return new ObjectMapper().readValue(messageBody, OrderEmailDto.class);
    }
}
