package com.example.backend.mapper;

import com.catering.commons.domain.event.OrderCreatedEvent;
import com.example.backend.domain.entity.Order;
import com.example.backend.model.OrderResponse;
import com.example.backend.service.interfaces.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class OrderMapper {
    private final OrderPositionMapper orderPositionMapper;
    private final OrderService orderService;
    public OrderResponse mapEntityToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .purchaserEmail(order.getPurchaserEmail())
                .purchaserPhoneNumber(order.getPurchaserPhoneNumber())
                .purchaserName(order.getPurchaserName())
                .purchaserSurname(order.getPurchaserSurname())
                .address(order.getAddress())
                .preferredDeliveryTime(order.getPreferredDeliveryTime())
                .orderPositions(order.getOrderPositions().stream()
                        .map(orderPositionMapper::mapEntityToResponseDto)
                        .collect(Collectors.toList()))
                .pricePerMonthPerOrder(orderService.calculatePricePerMonthPerOrder(order))
                .build();
    }
    public Order mapEventToEntity(OrderCreatedEvent event) {
        Order order = Order.builder()
                .id(event.getOrderId())
                .purchaserEmail(event.getPurchaserEmail())
                .purchaserPhoneNumber(event.getPurchaserPhoneNumber())
                .purchaserName(event.getPurchaserName())
                .purchaserSurname(event.getPurchaserSurname())
                .address(event.getAddress())
                .preferredDeliveryTime(event.getPreferredDeliveryTime())
                .orderPositions(event.getOrderPositions().stream()
                        .map(orderPositionMapper::mapEventDtoToEntity)
                        .toList())
                .build();

        order.getOrderPositions().forEach(position -> position.setOrder(order));

        return order;
    }
}
