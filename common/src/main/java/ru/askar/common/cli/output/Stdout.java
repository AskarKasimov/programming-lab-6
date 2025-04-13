package ru.askar.common.cli.output;

/** Класс для вывода ответов CLI в консоль. */
public class Stdout implements OutputWriter {
    @Override
    public void write(Object message) {
        if (message instanceof String) {
            System.out.println((String) message);
        } else if (message instanceof Exception) {
            System.err.println(((Exception) message).getMessage());
        } else {
            System.out.println(message);
        }
    }
}
