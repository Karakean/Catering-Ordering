package com.example.backend.command.create;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateCateringCommand {
    private final String name;
    private final String description;
    private final int calories;
    private final double pricePerMonth;

    @JsonCreator
    public CreateCateringCommand(@JsonProperty("name") String name,
                                 @JsonProperty("description") String description,
                                 @JsonProperty("calories") int calories,
                                 @JsonProperty("pricePerMonth") double pricePerMonth) {
        this.name = name;
        this.description = description;
        this.calories = calories;
        this.pricePerMonth = pricePerMonth;
    }
}
