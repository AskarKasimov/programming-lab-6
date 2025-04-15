package ru.askar.common.cli;

import java.io.Serializable;

public enum CommandResponseCode implements Serializable {
    HIDDEN,
    INFO,
    SUCCESS,
    WARNING,
    ERROR;
}
