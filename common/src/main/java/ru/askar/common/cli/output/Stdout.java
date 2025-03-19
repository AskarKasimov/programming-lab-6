package ru.askar.common.cli.output;

/**
 * Класс для вывода ответов CLI в консоль.
 */
public class Stdout implements OutputWriter {
    @Override
    public void write(String message) {
        System.out.print(message);
    }

    @Override
    public void writeln(String message) {
        System.out.println(message);
    }

    @Override
    public void writeOnSuccess(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }

    @Override
    public void writeOnFail(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }

    @Override
    public void writeOnWarning(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
    }
}
