package ru.askar.serverLab6;

import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.cli.CommandParser;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.cli.output.Stdout;
import ru.askar.serverLab6.collection.CollectionManager;
import ru.askar.serverLab6.collection.DataReader;
import ru.askar.serverLab6.collection.JsonReader;
import ru.askar.serverLab6.serverCommand.ServerStatusCommand;
import ru.askar.serverLab6.serverCommand.StartServerCommand;
import ru.askar.serverLab6.serverCommand.StopServerCommand;
import ru.askar.serverLab6.connection.ServerHandler;
import ru.askar.serverLab6.connection.TcpServerHandler;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        OutputWriter outputWriter = new Stdout();
        String filePath = System.getenv("COLLECTION_PATH");
        if (filePath == null) {
            outputWriter.writeOnFail("Переменная окружения COLLECTION_PATH не установлена");
            return;
        }
        outputWriter.writeOnSuccess("Файл: " + filePath);

        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
        } catch (FileNotFoundException | SecurityException e) {
            outputWriter.writeOnFail("Файл не удаётся прочитать: " + e.getMessage());
        }

        DataReader dataReader = new JsonReader(filePath, bufferedInputStream);
        if (bufferedInputStream == null) {
            dataReader = null;
        }

        CollectionManager collectionManager = null;
        try {
            collectionManager = new CollectionManager(dataReader);
        } catch (Exception e) {
            outputWriter.writeOnFail(e.getMessage());
        } finally {
            try {
                if (bufferedInputStream != null) bufferedInputStream.close();
            } catch (IOException e) {
                outputWriter.writeOnFail("Ошибка при закрытии файла: " + e.getMessage());
            }
        }
        if (collectionManager == null) {
            try {
                collectionManager = new CollectionManager(null);
            } catch (Exception e) {
                // ignored
            }
        }
        if (collectionManager.getCollection().isEmpty()) {
            outputWriter.writeOnWarning("Коллекция пуста");
        }
        CommandExecutor commandExecutor = new CommandExecutor(outputWriter);
        CommandParser commandParser = new CommandParser();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        InputReader inputReader = new InputReader(commandExecutor, commandParser, bufferedReader);

        ServerHandler serverHandler = new TcpServerHandler(collectionManager);

        commandExecutor.register(new StartServerCommand("start", inputReader, serverHandler));
        commandExecutor.register(new ServerStatusCommand("status", inputReader, serverHandler));
        commandExecutor.register(new StopServerCommand("stop", inputReader, serverHandler));


        try {
            inputReader.process();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
