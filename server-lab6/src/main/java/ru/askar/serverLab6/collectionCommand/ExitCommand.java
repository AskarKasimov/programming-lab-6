package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.exception.ExitCLIException;

public class ExitCommand extends CollectionCommand {
    public ExitCommand() {
        super("exit", 0, "exit - завершить программу (без сохранения в файл)");
    }

    @Override
    public void execute(String[] args) throws ExitCLIException {
        throw new ExitCLIException();
    }
}
