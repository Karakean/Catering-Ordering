package com.catering.commons.domain.event;

import com.catering.commons.dto.OrderPositionEventDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
@Getter
@NoArgsConstructor
public class OrderCreatedEvent implements AbstractEvent {
    private Long orderId;
    private String purchaserEmail;
    private String purchaserPhoneNumber;
    private String purchaserName;
    private String purchaserSurname;
    private String address;
    private String preferredDeliveryTime;
    private List<OrderPositionEventDto> orderPositions;
    //private String status;
}
