package com.example.backend.handler;

import com.example.backend.mapper.CateringMapper;
import com.example.backend.mapper.OrderMapper;
import com.example.backend.model.CateringResponse;
import com.example.backend.model.OrderResponse;
import com.example.backend.service.interfaces.CateringService;
import com.example.backend.service.interfaces.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.example.backend.config.RabbitMqConfig.QUERY_QUEUE_NAME;
//import static com.example.backend.config.RabbitMqConfig.QUERY_RESPONSE_QUEUE_NAME;

@AllArgsConstructor
@Component
public class QueryHandler {
    private final OrderService orderService;
    private final CateringService cateringService;
    private final OrderMapper orderMapper;
    private final CateringMapper cateringMapper;

    @RabbitListener(queues = QUERY_QUEUE_NAME)
    public Message handleQuery(Message message) {
        try {
            Map<String, Object> headers = message.getMessageProperties().getHeaders();
            String queryType = (String) headers.get("query_type");
            switch (queryType) {
                case "read_all_orders" -> {
                    return handleReadAllOrdersQuery(message);
                }
                case "read_all_caterings" -> {
                    return handleReadAllCateringsQuery(message);
                }
                default -> System.out.println("Unknown query type or query_type header does not exist.");
            }
        } catch (Exception exception) {
            System.out.println("Failed to process query.\n");
            exception.printStackTrace();
        }
        String returnMessage = "Failed to process query. Unknown query type or query_type header does not exist.";
        return new Message(returnMessage.getBytes());
    }

    private Message handleReadAllOrdersQuery(Message message) throws JsonProcessingException {
        List<OrderResponse> orderList = orderService.findAll().stream()
                .map(orderMapper::mapEntityToResponse)
                .toList();
        System.out.println("Sending all orders...");
        String jsonMessage = new ObjectMapper().writeValueAsString(orderList);
        return new Message(jsonMessage.getBytes());
    }

    private Message handleReadAllCateringsQuery(Message message) throws JsonProcessingException {
        List<CateringResponse> cateringList = cateringService.findAll().stream()
                .map(cateringMapper::mapEntityToResponseDto)
                .toList();
        System.out.println("Sending all caterings...");
        String jsonMessage = new ObjectMapper().writeValueAsString(cateringList);
        return new Message(jsonMessage.getBytes());
    }
}
