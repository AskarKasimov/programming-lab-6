package ru.askar.common;

public record CommandAsList(String name, int args, String info, Class<?> classToFill) {
}
