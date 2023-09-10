//package com.example.emailservice.dto;
//
//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.Getter;
//
//@Getter
//public class OrderPositionEmailDto {
//    private final String cateringName;
//    private final int quantity;
//    private final int calories;
//    private final double pricePerMonth;
//
//    @JsonCreator
//    public OrderPositionEmailDto(@JsonProperty("cateringName") String cateringName,
//                                 @JsonProperty("quantity") int quantity,
//                                 @JsonProperty("calories") int calories,
//                                 @JsonProperty("pricePerMonth") int pricePerMonth) {
//        this.cateringName = cateringName;
//        this.quantity = quantity;
//        this.calories = calories;
//        this.pricePerMonth = pricePerMonth;
//    }
//}
