package ru.askar.common;

import java.io.Serializable;

public record CommandResponse(int code, String response) implements Serializable {}
