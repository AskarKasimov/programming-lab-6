package ru.askar.common.object;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.exception.UserRejectedToFillFieldsException;

public class Coordinates implements Serializable {
    private Float x; // Поле не может быть null
    private Float y; // Максимальное значение поля: 654, Поле не может быть null

    @JsonCreator
    public Coordinates(@JsonProperty("x") Float x, @JsonProperty("y") BigDecimal y)
            throws InvalidInputFieldException {
        setX(x);
        setY(y);
    }

    private Coordinates() {}

    /**
     * Создание экземпляра с пользовательским вводом.
     *
     * @param outputWriter - способ печати ответа
     * @param inputReader - способ считывания входных данных
     * @return - созданный Coordinates
     */
    public static Coordinates createCoordinates(
            OutputWriter outputWriter, InputReader inputReader, boolean scriptMode)
            throws UserRejectedToFillFieldsException {
        Coordinates coordinates = new Coordinates();
        outputWriter.write("Ввод координат: ");
        coordinates.requestX(outputWriter, inputReader, scriptMode);
        coordinates.requestY(outputWriter, inputReader, scriptMode);
        return coordinates;
    }

    private void requestX(OutputWriter outputWriter, InputReader inputReader, boolean scriptMode)
            throws UserRejectedToFillFieldsException {
        Float x;
        do {
            outputWriter.write("Введите x: ");
            try {
                x = inputReader.getInputFloat();
                this.setX(x);
            } catch (IllegalArgumentException e) {
                x = null;
                if (scriptMode) {
                    throw new UserRejectedToFillFieldsException();
                }
                outputWriter.write(
                        OutputWriter.ANSI_RED + e.getMessage() + OutputWriter.ANSI_RESET);
                outputWriter.write(
                        OutputWriter.ANSI_YELLOW
                                + "Хотите попробовать еще раз? (y/n): "
                                + OutputWriter.ANSI_RESET);
                String answer = inputReader.getInputString();
                if (answer != null && !answer.equalsIgnoreCase("y")) {
                    throw new UserRejectedToFillFieldsException();
                }
            }
        } while (x == null);
    }

    private void requestY(OutputWriter outputWriter, InputReader inputReader, boolean scriptMode)
            throws UserRejectedToFillFieldsException {
        BigDecimal y;
        do {
            outputWriter.write("Введите y: ");
            try {
                y = inputReader.getInputBigDecimal();
                this.setY(y);
            } catch (IllegalArgumentException e) {
                y = null;
                if (scriptMode) {
                    throw new UserRejectedToFillFieldsException();
                }
                outputWriter.write(
                        OutputWriter.ANSI_RED + e.getMessage() + OutputWriter.ANSI_RESET);
                outputWriter.write(
                        OutputWriter.ANSI_YELLOW
                                + "Хотите попробовать еще раз? (y/n): "
                                + OutputWriter.ANSI_RESET);
                String answer = inputReader.getInputString();
                if (answer != null && !answer.equalsIgnoreCase("y")) {
                    throw new UserRejectedToFillFieldsException();
                }
            }
        } while (y == null);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Objects.equals(x, that.x) && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Координаты" + ": x=" + x + ", y=" + y;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y.floatValue();
    }
}
