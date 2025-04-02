package ru.askar.clientLab6;

import ru.askar.clientLab6.clientCommand.ClientStartCommand;
import ru.askar.clientLab6.connection.TcpClientHandler;
import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.cli.CommandParser;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.cli.output.Stdout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        OutputWriter outputWriter = new Stdout();

        CommandExecutor clientCommandExecutor = new CommandExecutor(outputWriter);
        CommandParser commandParser = new CommandParser();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        InputReader inputReader = new InputReader(clientCommandExecutor, commandParser, bufferedReader);

        TcpClientHandler tcpClientHandler = new TcpClientHandler();

        clientCommandExecutor.register(new ClientStartCommand(inputReader, tcpClientHandler));


        try {
            inputReader.process();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
