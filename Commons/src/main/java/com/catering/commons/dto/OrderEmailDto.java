package com.catering.commons.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
@Getter
@NoArgsConstructor
public class OrderEmailDto {
    private Long orderId;
    private String purchaserEmail;
    private String purchaserPhoneNumber;
    private String purchaserName;
    private String purchaserSurname;
    private String address;
    private String preferredDeliveryTime;
    private List<OrderPositionEmailDto> orderPositions;
}
