package com.catering.commons.domain.event;

import lombok.*;

@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
@Getter
@NoArgsConstructor
public class CateringCreatedEvent implements AbstractEvent {
    private Long cateringId;
    private String name;
    private String description;
    private int calories;
    private double pricePerMonth;
}
