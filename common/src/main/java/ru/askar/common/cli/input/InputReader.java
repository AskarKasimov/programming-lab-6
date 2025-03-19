package ru.askar.common.cli.input;

import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.cli.CommandParser;
import ru.askar.common.cli.ParsedCommand;
import ru.askar.common.exception.*;
import ru.askar.common.object.Coordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

public class InputReader {
    /**
     * Класс, ответственный за чтение ввода от пользователя и исполнение команд.
     */
    private final CommandExecutor commandExecutor;
    private final CommandParser commandParser;
    private boolean scriptMode = false;
    private BufferedReader bufferedReader;

    /**
     * @param commandExecutor - класс для выполнения команд.
     * @param commandParser   - класс для парсинга команд.
     * @param bufferedReader  - класс для чтения ввода.
     */
    public InputReader(CommandExecutor commandExecutor, CommandParser commandParser, BufferedReader bufferedReader) {
        this.commandExecutor = commandExecutor;
        this.commandParser = commandParser;
        this.bufferedReader = bufferedReader;
    }

    public boolean isScriptMode() {
        return scriptMode;
    }

    public void setScriptMode(boolean scriptMode) {
        this.scriptMode = scriptMode;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    /**
     * Считыватель ввода строки.
     *
     * @return - строка, введенная пользователем.
     * @throws IllegalArgumentException - если произошла ошибка ввода.
     * @see BufferedReader
     */
    public String getInputString() {
        try {
            String line = bufferedReader.readLine();
            if (line != null && line.isEmpty()) {
                return null;
            }
            return line;
        } catch (IOException e) {
            throw new IllegalArgumentException("Ошибка ввода");
        }
    }

    /**
     * Считыватель ввода числа с плавающей точкой.
     *
     * @see Coordinates
     */
    public float getInputFloat() {
        // Сделано по большей части для Y и ограничения на 654.00000000000001 и прочую дичь
        try {
            float value = new BigDecimal(bufferedReader.readLine()).floatValue();
            if (Float.isInfinite(value)) {
                throw new IllegalArgumentException("Число слишком большое");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Требуется число с точкой");
        } catch (IOException e) {
            throw new IllegalArgumentException("Ошибка ввода");
        }
    }

    public BigDecimal getInputBigDecimal() {
        // Сделано по большей части для Y и ограничения на 654.00000000000001 и прочую дичь
        try {
            return new BigDecimal(bufferedReader.readLine());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Требуется число с точкой");
        } catch (IOException e) {
            throw new IllegalArgumentException("Ошибка ввода");
        }
    }

    /**
     * Выполнение поступающих команд.
     */
    public void process() throws IOException {
        String line;
        if (!scriptMode)
            commandExecutor.getOutputWriter().write("> ");
        while ((line = bufferedReader.readLine()) != null) {
//            System.out.println(line);
            try {
                ParsedCommand parsedCommand;
                try {
                    parsedCommand = commandParser.parse(line.split(" "));
                } catch (InvalidCommandException e) {
                    commandExecutor.getOutputWriter().writeOnFail(e.getMessage());
                    continue;
                }
                if (parsedCommand.args().length != commandExecutor.getCommand(parsedCommand.name()).getArgsCount()) {
                    commandExecutor.getOutputWriter().writeOnFail("Неверное количество аргументов: для команды " + parsedCommand.name() + " требуется " + commandExecutor.getCommand(parsedCommand.name()).getArgsCount());
                    continue;
                }
                commandExecutor.getCommand(parsedCommand.name()).execute(parsedCommand.args());
            } catch (CollectionIsEmptyException | NoSuchCommandException | IOException |
                     NoSuchKeyException | IllegalArgumentException | InvalidInputFieldException e) {
                commandExecutor.getOutputWriter().writeOnFail(e.getMessage());
            } catch (UserRejectedToFillFieldsException e) {
                commandExecutor.getOutputWriter().writeOnWarning("Возврат в CLI");
            } catch (ExitCLIException e) {
                break;
            } finally {
                if (!scriptMode)
                    commandExecutor.getOutputWriter().write("> ");
            }
        }
    }
}
