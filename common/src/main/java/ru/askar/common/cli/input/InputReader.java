package ru.askar.common.cli.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import ru.askar.common.CommandResponse;
import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.cli.CommandParser;
import ru.askar.common.cli.ParsedCommand;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.ExitCLIException;
import ru.askar.common.exception.InvalidCommandException;
import ru.askar.common.exception.NoSuchCommandException;
import ru.askar.common.object.Coordinates;

public class InputReader {
    /** Класс, ответственный за чтение ввода от пользователя и исполнение команд. */
    private final CommandExecutor commandExecutor;

    private final CommandParser commandParser;
    private boolean scriptMode = false;
    private BufferedReader bufferedReader;

    /**
     * @param commandExecutor - класс для выполнения команд.
     * @param commandParser - класс для парсинга команд.
     * @param bufferedReader - класс для чтения ввода.
     */
    public InputReader(
            CommandExecutor commandExecutor,
            CommandParser commandParser,
            BufferedReader bufferedReader) {
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

    /** Выполнение поступающих команд. */
    public void process() throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            try {
                ParsedCommand parsedCommand;
                try {
                    parsedCommand = commandParser.parse(line.split(" "));
                } catch (InvalidCommandException e) {
                    commandExecutor
                            .getOutputWriter()
                            .write(
                                    OutputWriter.ANSI_RED
                                            + e.getMessage()
                                            + OutputWriter.ANSI_RESET);
                    continue;
                }
                if (parsedCommand.args().length
                        != commandExecutor.getCommand(parsedCommand.name()).getArgsCount()) {
                    commandExecutor
                            .getOutputWriter()
                            .write(
                                    OutputWriter.ANSI_RED
                                            + "Неверное количество аргументов: для команды "
                                            + parsedCommand.name()
                                            + " требуется "
                                            + commandExecutor
                                                    .getCommand(parsedCommand.name())
                                                    .getArgsCount()
                                            + OutputWriter.ANSI_RESET);
                    continue;
                }
                CommandResponse commandResponse =
                        commandExecutor
                                .getCommand(parsedCommand.name())
                                .execute(parsedCommand.args());
                switch (commandResponse.code()) {
                    case HIDDEN -> {
                        // спрятанное сообщение
                    }
                    case INFO -> {
                        commandExecutor.getOutputWriter().write(commandResponse.response());
                    }
                    case SUCCESS -> {
                        commandExecutor
                                .getOutputWriter()
                                .write(
                                        OutputWriter.ANSI_GREEN
                                                + commandResponse.response()
                                                + OutputWriter.ANSI_RESET);
                    }
                    case WARNING -> {
                        commandExecutor
                                .getOutputWriter()
                                .write(
                                        OutputWriter.ANSI_YELLOW
                                                + commandResponse.response()
                                                + OutputWriter.ANSI_RESET);
                    }
                    case ERROR -> {
                        commandExecutor
                                .getOutputWriter()
                                .write(
                                        OutputWriter.ANSI_RED
                                                + commandResponse.response()
                                                + OutputWriter.ANSI_RESET);
                    }
                    default -> {
                        commandExecutor
                                .getOutputWriter()
                                .write(
                                        OutputWriter.ANSI_RED
                                                + "Команда вернула неизвестный код: "
                                                + commandResponse.code()
                                                + OutputWriter.ANSI_RESET);
                    }
                }
            } catch (NoSuchCommandException e) {
                commandExecutor
                        .getOutputWriter()
                        .write(OutputWriter.ANSI_RED + e.getMessage() + OutputWriter.ANSI_RESET);
            } catch (ExitCLIException e) {
                commandExecutor
                        .getOutputWriter()
                        .write(OutputWriter.ANSI_YELLOW + e.getMessage() + OutputWriter.ANSI_RESET);
                return;
            }
        }
    }
}
