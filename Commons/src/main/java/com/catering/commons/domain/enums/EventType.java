package com.catering.commons.domain.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EventType implements Serializable {
    CREATE_CATERING("CREATE_CATERING"),
    CREATE_ORDER("CREATE_ORDER"),
    UPDATE_CATERING("UPDATE_CATERING"),
    UPDATE_ORDER("UPDATE_ORDER"),
    DELETE_CATERING("DELETE_CATERING"),
    DELETE_ORDER("DELETE_ORDER");

    private final String displayName;

    public static EventType fromDisplayName(String searched) {
        return Arrays.stream(EventType.values())
                .filter(eventType -> eventType.displayName.equals(searched))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No EventType with display name " + searched + " found"));
    }
}
