package com.example.backend.mapper;

import com.catering.commons.domain.event.OrderCreatedEvent;
import com.catering.commons.dto.OrderEmailDto;
import com.catering.commons.exception.CateringNotFoundException;
import com.example.backend.command.create.CreateOrderCommand;
import com.example.backend.domain.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class OrderMapper {
    private final OrderPositionMapper orderPositionMapper;

    public Order mapCommandToEntity(CreateOrderCommand command) throws CateringNotFoundException {
        Order order = Order.builder()
                .purchaserEmail(command.getPurchaserEmail())
                .purchaserPhoneNumber(command.getPurchaserPhoneNumber())
                .purchaserName(command.getPurchaserName())
                .purchaserSurname(command.getPurchaserSurname())
                .address(command.getAddress())
                .preferredDeliveryTime(command.getPreferredDeliveryTime())
                .orderPositions(command.getOrderPositions().stream()
                        .map(orderPositionMapper::mapCommandDtoToEntity)
                        .collect(Collectors.toList()))
                .build();
        order.getOrderPositions().forEach(position -> position.setOrder(order));
        return order;
    }

    public OrderCreatedEvent mapEntityToEvent(Order order) {
        return OrderCreatedEvent.builder()
                .orderId(order.getId())
                .purchaserEmail(order.getPurchaserEmail())
                .purchaserPhoneNumber(order.getPurchaserPhoneNumber())
                .purchaserName(order.getPurchaserName())
                .purchaserSurname(order.getPurchaserSurname())
                .address(order.getAddress())
                .preferredDeliveryTime(order.getPreferredDeliveryTime())
                .orderPositions(order.getOrderPositions().stream()
                        .map(orderPositionMapper::mapEntityToEventDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public OrderEmailDto mapEntityToEmailDto(Order order) {
        return OrderEmailDto.builder()
                .orderId(order.getId())
                .purchaserEmail(order.getPurchaserEmail())
                .purchaserPhoneNumber(order.getPurchaserPhoneNumber())
                .purchaserName(order.getPurchaserName())
                .purchaserSurname(order.getPurchaserSurname())
                .address(order.getAddress())
                .preferredDeliveryTime(order.getPreferredDeliveryTime())
                .orderPositions(order.getOrderPositions().stream()
                        .map(position -> orderPositionMapper.mapEntityToEmailDto(position, position.getCatering()))
                        .collect(Collectors.toList()))
                .build();
    }
}
