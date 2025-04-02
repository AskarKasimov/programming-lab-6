package ru.askar.common.exception;

import ru.askar.common.cli.output.OutputWriter;

public class InvalidInputFieldException extends Exception {
    public InvalidInputFieldException(String message) {
        super(OutputWriter.ANSI_RED + message + OutputWriter.ANSI_RESET);
    }
}
