//package com.example.backend.model.dto;
//
//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.Builder;
//import lombok.Getter;
//
//import java.io.Serializable;
//
//@Builder
//@Getter
//public class OrderPositionDto implements Serializable {
//    private final Long orderPositionId;
//    private final Long cateringId;
//    private final int quantity;
//
//    @JsonCreator
//    public OrderPositionDto(@JsonProperty("orderPositionId") Long orderPositionId,
//                            @JsonProperty("cateringId") Long cateringId,
//                            @JsonProperty("quantity") int quantity) {
//        this.orderPositionId = orderPositionId;
//        this.cateringId = cateringId;
//        this.quantity = quantity;
//    }
//}
