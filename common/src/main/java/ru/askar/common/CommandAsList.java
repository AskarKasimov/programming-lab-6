package ru.askar.common;

import java.io.Serializable;

public record CommandAsList(String name, int args, String info, boolean needObject)
        implements Serializable {}
