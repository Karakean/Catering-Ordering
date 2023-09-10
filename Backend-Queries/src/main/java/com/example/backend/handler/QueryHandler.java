package com.example.backend.handler;

import com.example.backend.mapper.CateringMapper;
import com.example.backend.mapper.OrderMapper;
import com.example.backend.model.CateringResponse;
import com.example.backend.model.OrderResponse;
import com.example.backend.service.interfaces.CateringService;
import com.example.backend.service.interfaces.NotificationService;
import com.example.backend.service.interfaces.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.example.backend.config.QueuesConfig.QUERY_QUEUE_NAME;

@AllArgsConstructor
@Component
public class QueryHandler {
    private final OrderService orderService;
    private final CateringService cateringService;
    private final NotificationService notificationService;
    private final OrderMapper orderMapper;
    private final CateringMapper cateringMapper;

    @RabbitListener(queues = QUERY_QUEUE_NAME)
    public void handleQuery(Message message) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        String queryType = (String) headers.get("query_type");
        switch (queryType) {
            case "read_all_orders":
                handleReadAllOrdersQuery(message);
            case "read_all_caterings":
                handleReadAllCateringsQuery(message);
        }
    }

    private void handleReadAllOrdersQuery(Message message) {
        List<OrderResponse> orderList = orderService.findAll().stream()
                .map(orderMapper::mapEntityToResponse)
                .toList();
        notificationService.sendReadAllOrdersQueryResponse(orderList);
    }

    private void handleReadAllCateringsQuery(Message message) {
        List<CateringResponse> cateringList = cateringService.findAll().stream()
                .map(cateringMapper::mapEntityToResponseDto)
                .toList();
        notificationService.sendReadAllCateringsQueryResponse(cateringList);
    }
}
