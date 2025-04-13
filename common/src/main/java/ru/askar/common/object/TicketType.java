package ru.askar.common.object;

import java.util.Arrays;
import java.util.stream.Collectors;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.UserRejectedToFillFieldsException;

public enum TicketType {
    VIP,
    USUAL,
    BUDGETARY,
    CHEAP;

    /** Получить доступные типы в строковом представлении */
    public static String getStringValues() {
        return Arrays.stream(values()).map(Enum::name).collect(Collectors.joining(","));
    }

    /**
     * Создание экземпляра с пользовательским вводом. Если запрашиваемого типа нет, предлагается
     * выбрать ещё раз.
     *
     * @param outputWriter - способ печати ответа
     * @param inputReader - способ считывания входных данных
     * @return требуемый TicketType
     */
    public static TicketType createTicketType(
            OutputWriter outputWriter, InputReader inputReader, boolean scriptMode)
            throws UserRejectedToFillFieldsException {
        outputWriter.write("Выберите тип билета (" + getStringValues() + "): ");
        TicketType ticketType;
        try {
            String value = inputReader.getInputString();
            if (value == null) {
                throw new IllegalArgumentException();
            }
            ticketType = valueOf(value);
        } catch (IllegalArgumentException e) {
            if (scriptMode) {
                throw new UserRejectedToFillFieldsException();
            }
            outputWriter.write(OutputWriter.ANSI_RED + "Такого типа нет" + OutputWriter.ANSI_RESET);
            outputWriter.write(
                    OutputWriter.ANSI_YELLOW
                            + "Хотите попробовать еще раз? (y/n): "
                            + OutputWriter.ANSI_RESET);
            String answer = inputReader.getInputString();
            if (answer != null && !answer.equalsIgnoreCase("y")) {
                throw new UserRejectedToFillFieldsException();
            }
            return createTicketType(outputWriter, inputReader, scriptMode);
        }
        return ticketType;
    }
}
