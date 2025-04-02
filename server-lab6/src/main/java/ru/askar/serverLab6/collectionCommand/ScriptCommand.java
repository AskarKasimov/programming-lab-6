package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.Command;
import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.cli.output.VoidOutput;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScriptCommand extends Command {
    private final CommandExecutor commandExecutor;

    public ScriptCommand(CommandExecutor commandExecutor, InputReader inputReader) {
        super("execute_script", 1, inputReader);
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

    @Override
    public String getInfo() {
        return "execute_script file_name - считать и исполнить скрипт из указанного файла";
    }
}
