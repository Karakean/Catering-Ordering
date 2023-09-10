package com.catering.commons.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
@Getter
@NoArgsConstructor
public class OrderEmailDto implements Serializable {
    private Long orderId;
    private String purchaserEmail;
    private String purchaserPhoneNumber;
    private String purchaserName;
    private String purchaserSurname;
    private String address;
    private String preferredDeliveryTime;
    private List<OrderPositionEmailDto> orderPositions;
}
