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
import ru.askar.serverLab6.connection.ServerOutputWriter;
import ru.askar.serverLab6.connection.TcpServerHandler;
import ru.askar.serverLab6.serverCommand.*;

public class Main {

    public static void main(String[] args) {
        String filePath = System.getenv("COLLECTION_PATH");
        if (filePath == null) {
            System.out.println(
                    OutputWriter.ANSI_RED
                            + "Переменная окружения COLLECTION_PATH не установлена"
                            + OutputWriter.ANSI_RESET);
            return;
        }
        System.out.println(OutputWriter.ANSI_GREEN + "Файл: " + filePath + OutputWriter.ANSI_RESET);

        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
        } catch (FileNotFoundException | SecurityException e) {
            System.out.println(
                    OutputWriter.ANSI_RED
                            + "Файл не удаётся прочитать: "
                            + e.getMessage()
                            + OutputWriter.ANSI_RESET);
        }

        DataReader dataReader = new JsonReader(filePath, bufferedInputStream);
        if (bufferedInputStream == null) {
            dataReader = null;
        }

        CollectionManager collectionManager = null;
        try {
            collectionManager = new CollectionManager(dataReader);
        } catch (Exception e) {
            System.out.println(OutputWriter.ANSI_RED + e.getMessage() + OutputWriter.ANSI_RESET);
        } finally {
            try {
                if (bufferedInputStream != null) bufferedInputStream.close();
            } catch (IOException e) {
                System.out.println(
                        OutputWriter.ANSI_RED
                                + "Ошибка при закрытии файла: "
                                + e.getMessage()
                                + OutputWriter.ANSI_RESET);
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
            System.out.println(
                    OutputWriter.ANSI_YELLOW + "Коллекция пуста" + OutputWriter.ANSI_RESET);
        }
        ArrayList<CommandAsList> commandList = new ArrayList<>();
        CommandExecutor<CollectionCommand> collectionCommandExecutor = new CommandExecutor<>();

        ServerHandler serverHandler = new TcpServerHandler(collectionCommandExecutor, commandList);
        OutputWriter outputWriter = new ServerOutputWriter(serverHandler);
        collectionCommandExecutor.setOutputWriter(outputWriter);
        collectionCommandExecutor.register(new HelpCommand(collectionCommandExecutor));
        collectionCommandExecutor.register(new InfoCommand(collectionManager));
        collectionCommandExecutor.register(new ShowCommand(collectionManager));
        collectionCommandExecutor.register(
                new InsertCommand(null, collectionManager, outputWriter));
        collectionCommandExecutor.register(
                new UpdateCommand(null, collectionManager, outputWriter));
        collectionCommandExecutor.register(new RemoveByKeyCommand(collectionManager));
        collectionCommandExecutor.register(new ClearCommand(collectionManager));
        collectionCommandExecutor.register(new ExitCommand(serverHandler));
        collectionCommandExecutor.register(
                new RemoveLowerCommand(null, collectionManager, outputWriter));
        collectionCommandExecutor.register(
                new ReplaceIfGreaterCommand(null, collectionManager, outputWriter));
        collectionCommandExecutor.register(new RemoveGreaterKeyCommand(collectionManager));
        collectionCommandExecutor.register(new FilterStartsWithNameCommand(collectionManager));
        collectionCommandExecutor.register(new PrintFieldAscendingEventCommand(collectionManager));
        collectionCommandExecutor.register(new PrintFieldDescendingTypeCommand(collectionManager));
        collectionCommandExecutor
                .getAllCommands()
                .forEach(
                        (name, command) -> {
                            commandList.add(
                                    new CommandAsList(
                                            command.getName(),
                                            command.getArgsCount(),
                                            command.isNeedObject()));
                        });

        CommandExecutor<ServerCommand> serverCommandExecutor = new CommandExecutor<>();
        OutputWriter stdout = new Stdout();
        serverCommandExecutor.setOutputWriter(stdout);
        CommandParser commandParser = new CommandParser();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        InputReader inputReader =
                new InputReader(serverCommandExecutor, commandParser, bufferedReader);

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
