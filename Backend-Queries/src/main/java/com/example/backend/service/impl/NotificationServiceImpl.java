//package com.example.backend.service.impl;
//
//import com.example.backend.model.CateringResponse;
//import com.example.backend.model.OrderResponse;
//import com.example.backend.service.interfaces.NotificationService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.AllArgsConstructor;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
////import static com.example.backend.config.RabbitMqConfig.QUERY_RESPONSE_QUEUE_NAME;
//
//@AllArgsConstructor
//@Service
//public class NotificationServiceImpl implements NotificationService {
//    private final RabbitTemplate rabbitTemplate;
//
//    public void sendReadAllOrdersQueryResponse(List<OrderResponse> orderList) throws JsonProcessingException {
//        String jsonMessage = new ObjectMapper().writeValueAsString(orderList);
//        //rabbitTemplate.convertAndSend(QUERY_RESPONSE_QUEUE_NAME, jsonMessage.getBytes());
//    }
//
////    public void sendReadAllCateringsQueryResponse(List<CateringResponse> cateringList) throws JsonProcessingException {
////        String jsonMessage = new ObjectMapper().writeValueAsString(cateringList);
////        rabbitTemplate.convertAndSend(QUERY_RESPONSE_QUEUE_NAME, jsonMessage.getBytes());
////    }
//}
