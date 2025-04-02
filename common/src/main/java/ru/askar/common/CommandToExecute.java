package ru.askar.common;

public record CommandToExecute(String name, String[] args, Class<?> filledClass) {
}
