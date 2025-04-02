package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.object.Command;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends CollectionCommand {
    private final CommandExecutor executor;

    public HelpCommand(CommandExecutor executor) {
        super("help", 0);
        this.executor = executor;
    }

    @Override
    public void execute(String[] args) {
        List<Command> commands = new ArrayList<>(executor.getAllCommands().values());
        commands.forEach(command -> outputWriter.writeOnSuccess(command.getInfo()));
    }

    @Override
    public String getInfo() {
        return "help - вывести справку по доступным командам";
    }
}
