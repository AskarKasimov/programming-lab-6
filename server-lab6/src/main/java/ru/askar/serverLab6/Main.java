package ru.askar.serverLab6;

import java.io.*;
import java.util.ArrayList;
import ru.askar.common.CommandAsList;
import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.cli.CommandParser;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.cli.output.Stdout;
import ru.askar.serverLab6.collection.CollectionManager;
import ru.askar.serverLab6.collection.DataReader;
import ru.askar.serverLab6.collection.JsonReader;
import ru.askar.serverLab6.collectionCommand.*;
import ru.askar.serverLab6.connection.ServerHandler;
import ru.askar.serverLab6.connection.TcpServerHandler;
import ru.askar.serverLab6.serverCommand.*;

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
        CommandExecutor<CollectionCommand> collectionCommandExecutor =
                new CommandExecutor<>(outputWriter);
        collectionCommandExecutor.register(new HelpCommand(collectionCommandExecutor));
        collectionCommandExecutor.register(new InfoCommand(collectionManager));
        collectionCommandExecutor.register(new ShowCommand(collectionManager));
        collectionCommandExecutor.register(new InsertCommand(collectionManager));
        collectionCommandExecutor.register(new UpdateCommand(collectionManager));
        collectionCommandExecutor.register(new RemoveByKeyCommand(collectionManager));
        collectionCommandExecutor.register(new ClearCommand(collectionManager));
        collectionCommandExecutor.register(new ExitCommand());
        collectionCommandExecutor.register(new RemoveLowerCommand(collectionManager));
        collectionCommandExecutor.register(new ReplaceIfGreaterCommand(collectionManager));
        collectionCommandExecutor.register(new RemoveGreaterKeyCommand(collectionManager));
        collectionCommandExecutor.register(new FilterStartsWithNameCommand(collectionManager));
        collectionCommandExecutor.register(new PrintFieldAscendingEventCommand(collectionManager));
        collectionCommandExecutor.register(new PrintFieldDescendingTypeCommand(collectionManager));

        CommandExecutor<ServerCommand> serverCommandExecutor = new CommandExecutor<>(outputWriter);
        CommandParser commandParser = new CommandParser();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        InputReader inputReader =
                new InputReader(serverCommandExecutor, commandParser, bufferedReader);
        ArrayList<CommandAsList> commandList = new ArrayList<>();
        collectionCommandExecutor
                .getAllCommands()
                .forEach(
                        (name, command) -> {
                            commandList.add(
                                    new CommandAsList(
                                            command.getName(),
                                            command.getArgsCount(),
                                            command.getInfo(),
                                            command.isNeedObject()));
                        });
        ServerHandler serverHandler = new TcpServerHandler(collectionCommandExecutor, commandList);

        serverCommandExecutor.register(new ServerStartCommand(serverHandler));
        serverCommandExecutor.register(new ServerStatusCommand(serverHandler));
        serverCommandExecutor.register(new ServerStopCommand(serverHandler));
        serverCommandExecutor.register(new ServerHelpCommand(serverHandler, serverCommandExecutor));
        serverCommandExecutor.register(new ServerExitCommand(serverHandler));

        try {
            inputReader.process();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
