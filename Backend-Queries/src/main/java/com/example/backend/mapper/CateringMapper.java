package com.example.backend.mapper;

import com.catering.commons.domain.event.CateringCreatedEvent;
import com.example.backend.domain.entity.Catering;
import com.example.backend.model.CateringResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CateringMapper {
    public Catering mapEventToEntity(CateringCreatedEvent event) {
        return Catering.builder()
                .id(event.getCateringId())
                .name(event.getName())
                .description(event.getDescription())
                .calories(event.getCalories())
                .pricePerMonth(event.getPricePerMonth())
                .build();
    }

    public CateringResponse mapEntityToResponseDto(Catering catering) {
        return CateringResponse.builder()
                .id(catering.getId())
                .name(catering.getName())
                .description(catering.getDescription())
                .calories(catering.getCalories())
                .pricePerMonth(catering.getPricePerMonth())
                .build();
    }
}
