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

@AllArgsConstructor
@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    private final CalculationService calculationService;
    private final JavaMailSender mailSender;

    private String createMailContentFromOrderInfo(OrderEmailDto orderInfo) {
        StringBuilder orderedCateringsTextBuilder = new StringBuilder();
        orderInfo.getOrderPositions().forEach
                (catering -> orderedCateringsTextBuilder.append(String.format(
                        """
                            Catering: %s
                            Zamówionych sztuk: %d
                            Kalorie (na sztukę): %d
                            Cena za miesiąc (na sztukę): %f
                            Cena za miesiąc (łącznie za rodzaj): %f
                        """,
                        catering.getCateringName(),
                        catering.getQuantity(),
                        catering.getCalories(),
                        catering.getPricePerMonth(),
                        calculationService.calculatePricePerMonthPerOrderPosition(catering)))
                        .append("\n\n"));
        String orderedCateringsText = orderedCateringsTextBuilder.toString();
        return "Zamówione cateringi:\n" +
                orderedCateringsText +
                "\nEmail: " + orderInfo.getPurchaserEmail() +
                "\nImię: " + orderInfo.getPurchaserName() +
                "\nNazwisko: " + orderInfo.getPurchaserSurname() +
                "\nAdres dostawy: " + orderInfo.getAddress() +
                "\nPreferowany czas dostawy: " + orderInfo.getPreferredDeliveryTime() +
                "\nCena za miesiąc (łącznie za wszystko): " + calculationService.calculatePricePerMonthPerOrder(orderInfo);
    }

    @Override
    public void sendEmail(OrderEmailDto orderInfo) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("mn.dev.184865@gmail.com");
        mail.setTo(orderInfo.getPurchaserEmail());
        mail.setSubject("Catering - Zamowienie nr. ");
        mail.setText(createMailContentFromOrderInfo(orderInfo));
        System.out.println(createMailContentFromOrderInfo(orderInfo));
        //mailSender.send(mail);
    }

    @Override
    public OrderEmailDto extractContentFromMessage(Message message) throws IOException {
        byte[] messageBody = message.getBody();
        return new ObjectMapper().readValue(messageBody, OrderEmailDto.class);
    }
}
