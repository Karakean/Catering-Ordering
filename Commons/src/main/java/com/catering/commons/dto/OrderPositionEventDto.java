package com.catering.commons.dto;

import lombok.*;

@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
@Getter
@NoArgsConstructor
public class OrderPositionEventDto {
    private Long orderPositionId;
    private Long cateringId;
    private int quantity;
}
