package ru.askar.common.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;

public record TransferredObjectFields(LinkedHashMap<String, PossibleTransferableTypes> fields)
        implements Serializable {}
