package ru.askar.clientLab6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import ru.askar.clientLab6.clientCommand.ClientCommand;
import ru.askar.clientLab6.clientCommand.ClientScriptCommand;
import ru.askar.clientLab6.clientCommand.ClientStartCommand;
import ru.askar.clientLab6.connection.ClientHandler;
import ru.askar.clientLab6.connection.TcpClientHandler;
import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.cli.CommandParser;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.cli.output.Stdout;

public class Main {
    public static void main(String[] args) {
        OutputWriter outputWriter = new Stdout();

        CommandExecutor<ClientCommand> clientCommandExecutor = new CommandExecutor<>();
        clientCommandExecutor.setOutputWriter(outputWriter);
        CommandParser commandParser = new CommandParser();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        InputReader inputReader =
                new InputReader(clientCommandExecutor, commandParser, bufferedReader);

        ClientHandler clientHandler = new TcpClientHandler(inputReader, clientCommandExecutor);

        clientCommandExecutor.register(new ClientStartCommand(clientHandler, outputWriter));
        clientCommandExecutor.register(new ClientScriptCommand(clientHandler, outputWriter));

        try {
            inputReader.process();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
