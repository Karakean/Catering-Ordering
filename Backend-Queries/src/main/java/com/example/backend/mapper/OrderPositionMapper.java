package com.example.backend.mapper;

import com.catering.commons.dto.OrderPositionEventDto;
import com.catering.commons.exception.CateringNotFoundException;
import com.example.backend.domain.entity.Catering;
import com.example.backend.domain.entity.OrderPosition;
import com.example.backend.model.OrderPositionResponse;
import com.example.backend.service.interfaces.CateringService;
import com.example.backend.service.interfaces.OrderPositionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class OrderPositionMapper {
    private final CateringService cateringService;
    private final OrderPositionService orderPositionService;

    public OrderPosition mapEventDtoToEntity(OrderPositionEventDto eventDto) throws CateringNotFoundException{
        Catering catering = cateringService.findById(eventDto.getCateringId())
                .orElseThrow(() -> new CateringNotFoundException("There is no catering with such id!"));

        return OrderPosition.builder()
                .id(eventDto.getOrderPositionId())
                .quantity(eventDto.getQuantity())
                .catering(catering)
                .build();
    }

    public OrderPositionResponse mapEntityToResponseDto(OrderPosition orderPosition) {
        Catering catering = orderPosition.getCatering();
        return OrderPositionResponse.builder()
                .cateringName(catering.getName())
                .quantity(orderPosition.getQuantity())
                .calories(catering.getCalories())
                .pricePerMonthPerCatering(catering.getPricePerMonth())
                .pricePerMonthPerOrderPosition(orderPositionService.calculatePricePerMonthPerOrderPosition(orderPosition))
                .build();
    }
}
