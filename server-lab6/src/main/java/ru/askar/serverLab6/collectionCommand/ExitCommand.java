package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.exception.ExitCLIException;

public class ExitCommand extends CollectionCommand {
    public ExitCommand() {
        super("exit", 0);
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
