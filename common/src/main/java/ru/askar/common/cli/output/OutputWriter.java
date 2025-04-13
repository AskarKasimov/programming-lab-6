package ru.askar.common.cli.output;

/** Описание метода вывода ответов CLI. */
public interface OutputWriter {
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_YELLOW = "\u001B[33m";
    String ANSI_RESET = "\u001B[0m";

    void write(Object message);
}
