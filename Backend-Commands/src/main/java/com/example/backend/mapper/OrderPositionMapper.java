package com.example.backend.mapper;

import com.catering.commons.dto.OrderPositionEmailDto;
import com.catering.commons.dto.OrderPositionEventDto;
import com.catering.commons.exception.CateringNotFoundException;
import com.example.backend.domain.entity.Catering;
import com.example.backend.domain.entity.OrderPosition;
import com.example.backend.dto.OrderPositionCommandDto;
import com.example.backend.service.interfaces.CateringService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class OrderPositionMapper {
    private final CateringService cateringService;

    public OrderPosition mapCommandDtoToEntity(OrderPositionCommandDto commandDto) throws CateringNotFoundException{
        Catering catering = cateringService.findById(commandDto.getCateringId())
                .orElseThrow(() -> new CateringNotFoundException("There is no catering with such id!"));

        return OrderPosition.builder()
                .quantity(commandDto.getQuantity())
                .catering(catering)
                .build();
    }

    public OrderPositionEventDto mapEntityToEventDto(OrderPosition orderPosition) {
        return OrderPositionEventDto.builder()
                .orderPositionId(orderPosition.getId())
                .cateringId(orderPosition.getCatering().getId())
                .quantity(orderPosition.getQuantity())
                .build();
    }

    public OrderPositionEmailDto mapEntityToEmailDto(OrderPosition orderPosition, Catering catering) {
        return OrderPositionEmailDto.builder()
                .cateringName(catering.getName())
                .quantity(orderPosition.getQuantity())
                .calories(catering.getCalories())
                .pricePerMonth(catering.getPricePerMonth())
                .build();
    }
}
