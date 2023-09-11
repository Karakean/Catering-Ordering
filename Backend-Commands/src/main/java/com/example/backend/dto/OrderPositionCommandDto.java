package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OrderPositionCommandDto {
    private final Long cateringId;
    private final int quantity;

    @JsonCreator
    public OrderPositionCommandDto(@JsonProperty("cateringId") Long cateringId,
                                   @JsonProperty("quantity") int quantity) {
        this.cateringId = cateringId;
        this.quantity = quantity;
    }
}
