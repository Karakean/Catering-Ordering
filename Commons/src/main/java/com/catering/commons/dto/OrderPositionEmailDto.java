package com.catering.commons.dto;

import lombok.*;

@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
@Getter
@NoArgsConstructor
public class OrderPositionEmailDto {
    private String cateringName;
    private int quantity;
    private int calories;
    private double pricePerMonth;
}
