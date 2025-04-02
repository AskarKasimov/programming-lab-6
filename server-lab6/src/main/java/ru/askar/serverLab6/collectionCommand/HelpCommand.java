package ru.askar.serverLab6.collectionCommand;

import java.util.ArrayList;
import java.util.List;
import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.object.Command;

public class HelpCommand extends CollectionCommand {
    private final CommandExecutor<CollectionCommand> executor;

    public HelpCommand(CommandExecutor<CollectionCommand> executor) {
        super("help", 0, "help - вывести справку по доступным командам");
        this.executor = executor;
    }

    @Override
    public void execute(String[] args) {
        List<Command> commands = new ArrayList<>(executor.getAllCommands().values());
        commands.forEach(command -> outputWriter.writeOnSuccess(command.getInfo()));
    }
}
