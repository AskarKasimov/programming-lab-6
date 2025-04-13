package ru.askar.common.dto;

import java.io.Serializable;

public record TransferredCommand(
        String name, String[] args, TransferredObjectFields objectFields, Object object)
        implements Serializable {}
