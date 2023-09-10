package com.example.backend.command.create;

import com.catering.commons.*;
import com.example.backend.dto.OrderPositionCommandDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderCommand {
    private final String purchaserEmail;
    private final String purchaserPhoneNumber;
    private final String purchaserName;
    private final String purchaserSurname;
    private final String address;
    private final String preferredDeliveryTime;
    private final List<OrderPositionCommandDto> orderPositions;

    @JsonCreator
    public CreateOrderCommand(@JsonProperty("purchaserEmail") String purchaserEmail,
                              @JsonProperty("purchaserPhoneNumber") String purchaserPhoneNumber,
                              @JsonProperty("purchaserName") String purchaserName,
                              @JsonProperty("purchaserSurname") String purchaserSurname,
                              @JsonProperty("address") String address,
                              @JsonProperty("preferredDeliveryTime") String preferredDeliveryTime,
                              @JsonProperty("orderPositions") List<OrderPositionCommandDto> orderPositions) {
        this.purchaserEmail = purchaserEmail;
        this.purchaserPhoneNumber = purchaserPhoneNumber;
        this.purchaserName = purchaserName;
        this.purchaserSurname = purchaserSurname;
        this.address = address;
        this.preferredDeliveryTime = preferredDeliveryTime;
        this.orderPositions = orderPositions;
    }
}
