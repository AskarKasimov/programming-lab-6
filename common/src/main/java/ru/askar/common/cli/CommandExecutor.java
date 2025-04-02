package ru.askar.common.cli;

import java.util.LinkedHashMap;
import java.util.Map;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.NoSuchCommandException;
import ru.askar.common.object.Command;

/** Класс для аккумулирования команд и предоставления к ним доступа. */
public class CommandExecutor<T extends Command> {
    private final OutputWriter outputWriter;
    private final LinkedHashMap<String, T> commands = new LinkedHashMap<>();
    private boolean scriptMode;

    /**
     * Создание реестра команд с регистрацией способа вывода.
     *
     * @param outputWriter - куда выводятся ответы CLI.
     */
    public CommandExecutor(OutputWriter outputWriter) {
        this.outputWriter = outputWriter;
        this.scriptMode = false;
    }

    public CommandExecutor(OutputWriter outputWriter, boolean scriptMode) {
        this(outputWriter);
        this.scriptMode = scriptMode;
    }

    public void setOutputWriterToCommands(OutputWriter outputWriter) {
        commands.forEach((name, command) -> command.setOutputWriter(outputWriter));
    }

    public OutputWriter getOutputWriter() {
        return outputWriter;
    }

    /**
     * Добавление команды.
     *
     * @param command - команда
     */
    public void register(T command) {
        command.setOutputWriter(this.outputWriter);
        command.setScriptMode(this.scriptMode);
        commands.put(command.getName(), command);
    }

    public void clearCommands() {
        commands.clear();
    }

    /**
     * Получить команду по названию
     *
     * @param name - название команды
     * @return экземпляр команды
     * @throws NoSuchCommandException - если нет команды с таким названием
     */
    public T getCommand(String name) throws NoSuchCommandException {
        T command = commands.get(name);
        if (command == null) {
            throw new NoSuchCommandException(name);
        }
        return commands.get(name);
    }

    /** Копия экземпляра мапы со всеми доступными командами */
    public Map<String, T> getAllCommands() {
        return new LinkedHashMap<>(commands);
    }
}
