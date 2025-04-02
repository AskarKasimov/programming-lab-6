package ru.askar.common;

import java.io.Serializable;

public record CommandResponse(String name, String response) implements Serializable {
}
