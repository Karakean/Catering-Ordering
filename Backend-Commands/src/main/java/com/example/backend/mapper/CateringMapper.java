package com.example.backend.mapper;

import com.catering.commons.domain.event.CateringCreatedEvent;
import com.example.backend.domain.entity.Catering;

import com.example.backend.command.create.CreateCateringCommand;
//import com.example.backend.domain.event.CateringCreatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CateringMapper {

    public Catering mapCommandToEntity(CreateCateringCommand command) {
        return Catering.builder()
                .name(command.getName())
                .description(command.getDescription())
                .calories(command.getCalories())
                .pricePerMonth(command.getPricePerMonth())
                .build();
    }

    public CateringCreatedEvent mapEntityToEvent(Catering catering) {
        return CateringCreatedEvent.builder()
                .cateringId(catering.getId())
                .name(catering.getName())
                .description(catering.getDescription())
                .calories(catering.getCalories())
                .pricePerMonth(catering.getPricePerMonth())
                .build();
    }

}
