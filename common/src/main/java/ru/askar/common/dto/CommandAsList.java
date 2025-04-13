package ru.askar.common.dto;

import java.io.Serializable;

public record CommandAsList(String name, int args, String info, boolean needObject)
        implements Serializable {}
