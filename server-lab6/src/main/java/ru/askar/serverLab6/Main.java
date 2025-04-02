package ru.askar.serverLab6;

import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.cli.CommandParser;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.cli.output.Stdout;
import ru.askar.serverLab6.collection.CollectionManager;
import ru.askar.serverLab6.collection.DataReader;
import ru.askar.serverLab6.collection.JsonReader;
import ru.askar.serverLab6.connection.ServerHandler;
import ru.askar.serverLab6.connection.TcpServerHandler;
import ru.askar.serverLab6.serverCommand.*;

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
        CommandExecutor serverCommandExecutor = new CommandExecutor(outputWriter);
        CommandParser commandParser = new CommandParser();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        InputReader inputReader = new InputReader(serverCommandExecutor, commandParser, bufferedReader);

        ServerHandler serverHandler = new TcpServerHandler();

        serverCommandExecutor.register(new StartServerCommand(inputReader, serverHandler));
        serverCommandExecutor.register(new ServerStatusCommand(inputReader, serverHandler));
        serverCommandExecutor.register(new StopServerCommand(inputReader, serverHandler));
        serverCommandExecutor.register(new ServerHelpCommand(inputReader, serverHandler, serverCommandExecutor));
        serverCommandExecutor.register(new ServerExitCommand(inputReader, serverHandler));


        try {
            inputReader.process();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
