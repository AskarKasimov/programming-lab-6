package ru.askar.serverLab6.collectionCommand;

import java.util.ArrayList;
import java.util.List;
import ru.askar.common.CommandResponse;
import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.object.Command;

public class SaveCommand extends CollectionCommand {
    private final CommandExecutor<CollectionCommand> executor;

    public SaveCommand(CommandExecutor<CollectionCommand> executor) {
        super("help", 0, "save - сохранить коллекцию в файл", null);
        this.executor = executor;
    }

    @Override
    public CommandResponse execute(String[] args) {
        StringBuilder builder = new StringBuilder();
        List<Command> commands = new ArrayList<>(executor.getAllCommands().values());
        commands.forEach(command -> builder.append(command.getInfo()).append("\n"));
        return new CommandResponse(0, builder.substring(0, builder.toString().length() - 1));
    }
}
