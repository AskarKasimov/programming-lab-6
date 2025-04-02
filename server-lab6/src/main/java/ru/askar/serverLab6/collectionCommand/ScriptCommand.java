package ru.askar.serverLab6.collectionCommand;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.cli.output.VoidOutput;

public class ScriptCommand extends CollectionCommand {
    private final CommandExecutor<CollectionCommand> commandExecutor;

    public ScriptCommand(CommandExecutor<CollectionCommand> commandExecutor) {
        super(
                "execute_script",
                1,
                "execute_script file_name - считать и исполнить скрипт из указанного файла");
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void execute(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(args[0]);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        BufferedReader lastBufferedReader = inputReader.getBufferedReader();
        OutputWriter lastOutputWriter = commandExecutor.getOutputWriter();
        boolean lastScriptMode = inputReader.isScriptMode();
        commandExecutor.setOutputWriterToCommands(new VoidOutput());
        inputReader.setBufferedReader(bufferedReader);
        inputReader.setScriptMode(true);
        inputReader.process();
        inputReader.setScriptMode(lastScriptMode);
        inputReader.setBufferedReader(lastBufferedReader);
        commandExecutor.setOutputWriterToCommands(lastOutputWriter);
    }
}
