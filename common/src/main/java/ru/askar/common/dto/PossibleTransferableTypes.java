package ru.askar.common.dto;

import java.io.Serializable;

public enum PossibleTransferableTypes implements Serializable {
    STRING("String"),
    INTEGER("Integer"),
    FLOAT("Float");

    private final String typeName;

    PossibleTransferableTypes(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
