package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class CateringResponse {
    private Long id;
    private String name;
    private String description;
    private int calories;
    private double pricePerMonth;
}
