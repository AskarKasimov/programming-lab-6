package ru.askar.serverLab6.command;

import ru.askar.common.Command;
import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.cli.input.InputReader;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends Command {
    private final CommandExecutor executor;

    public HelpCommand(CommandExecutor executor, InputReader inputReader) {
        super("help", 0, inputReader);
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
