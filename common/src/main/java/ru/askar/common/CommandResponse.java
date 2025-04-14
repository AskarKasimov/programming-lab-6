package ru.askar.common;

import java.io.Serializable;

/**
 * @param code -1 - не писать 0 - белый 1 - зелёный 2 - жёлтый 3 - красный
 * @param response
 */
public record CommandResponse(int code, String response) implements Serializable {}
