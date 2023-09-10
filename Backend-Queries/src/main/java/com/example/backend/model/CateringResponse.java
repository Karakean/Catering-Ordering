package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CateringResponse {
    private Long id;
    private String name;
    private String description;
    private int calories;
    private double pricePerMonth;
}
