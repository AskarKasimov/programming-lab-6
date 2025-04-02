package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.Command;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.exception.ExitCLIException;

public class ExitCommand extends Command {
    public ExitCommand(InputReader inputReader) {
        super("exit", 0, inputReader);
    }

    @Override
    public void execute(String[] args) throws ExitCLIException {
        throw new ExitCLIException();
    }

    @Override
    public String getInfo() {
        return "exit - завершить программу (без сохранения в файл)";
    }
}
