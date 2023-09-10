package com.example.backend.service.interfaces;

import com.example.backend.model.CateringResponse;
import com.example.backend.model.OrderResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

import static com.example.backend.config.QueuesConfig.QUERY_RESPONSE_QUEUE_NAME;

public interface NotificationService {
    void sendReadAllOrdersQueryResponse(List<OrderResponse> orderList) throws JsonProcessingException;
    void sendReadAllCateringsQueryResponse(List<CateringResponse> cateringList) throws JsonProcessingException;
}