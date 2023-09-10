package com.example.backend.mapper;

import com.catering.commons.domain.event.OrderCreatedEvent;
import com.catering.commons.dto.OrderEmailDto;
import com.example.backend.command.create.CreateOrderCommand;
import com.example.backend.domain.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class OrderMapper {
    private final OrderPositionMapper orderPositionMapper;
    //private final ClientService clientService;

//    public Order mapCommandToEntity(CreateOrderCommand command) throws ClientNotFoundException {
//        return Order.builder()
//                .preferredDeliveryTime(command.getPreferredDeliveryTime())
//                .status(StatusType.PENDING)
//                .client(clientService.findById(command.getClientId())
//                        .orElseThrow(() -> new ClientNotFoundException("There is no client with such id!")))
//                .build();
//    }
    public Order mapCommandToEntity(CreateOrderCommand command) {
        return Order.builder()
                .purchaserEmail(command.getPurchaserEmail())
                .purchaserPhoneNumber(command.getPurchaserPhoneNumber())
                .purchaserName(command.getPurchaserName())
                .purchaserSurname(command.getPurchaserSurname())
                .address(command.getAddress())
                .preferredDeliveryTime(command.getPreferredDeliveryTime())
                .build();
    }

//    public OrderCreatedEvent mapEntityToEvent(Order order) {
//        return OrderCreatedEvent.builder()
//                .orderId(order.getId())
//                //.purchaserName(order.getPurchaserName())
//                .chosenCatering(order.getChosenCatering())
//                .address(order.getAddress())
//                .preferredDeliveryTime(order.getPreferredDeliveryTime())
//                .build();
//    }
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
