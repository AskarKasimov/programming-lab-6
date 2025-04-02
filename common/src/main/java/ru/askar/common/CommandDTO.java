package ru.askar.common;

import java.io.Serializable;

// Общий DTO
public record CommandDTO(String command, String body) implements Serializable {
}
