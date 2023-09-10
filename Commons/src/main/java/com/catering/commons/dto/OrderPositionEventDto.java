package com.catering.commons.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
@Getter
public class OrderPositionEventDto implements Serializable {
    private final Long orderPositionId;
    private final Long cateringId;
    private final int quantity;
}
