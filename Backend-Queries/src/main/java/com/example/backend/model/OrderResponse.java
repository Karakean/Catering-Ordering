package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private String purchaserEmail;
    private String purchaserPhoneNumber;
    private String purchaserName;
    private String purchaserSurname;
    private String address;
    private String preferredDeliveryTime;
    private List<OrderPositionResponse> orderPositions;
    private double pricePerMonthPerOrder;
}
