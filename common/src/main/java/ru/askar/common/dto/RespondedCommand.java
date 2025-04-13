package ru.askar.common.dto;

import java.io.Serializable;

public record RespondedCommand(String name, String response) implements Serializable {}
