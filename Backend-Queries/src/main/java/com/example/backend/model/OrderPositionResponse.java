package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class OrderPositionResponse {
    private String cateringName;
    private int quantity;
    private int calories;
    private double pricePerMonthPerCatering;
    private double pricePerMonthPerOrderPosition;
}
