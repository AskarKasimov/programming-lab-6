package ru.askar.common;

import java.io.Serializable;

public record CommandToExecute(String name, String[] args, Class<?> filledClass)
        implements Serializable {}
