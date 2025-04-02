package ru.askar.common.cli.output;

public class VoidOutput implements OutputWriter {
    @Override
    public void write(String message) {}

    @Override
    public void writeln(String message) {}

    @Override
    public void writeOnSuccess(String message) {}

    @Override
    public void writeOnFail(String message) {}

    @Override
    public void writeOnWarning(String message) {}
}
