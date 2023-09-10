package com.catering.commons.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
@Getter
@NoArgsConstructor
public class OrderPositionEmailDto implements Serializable {
    private String cateringName;
    private int quantity;
    private int calories;
    private double pricePerMonth;
}
