package ru.askar.common;

import java.io.IOException;
import java.io.Serializable;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.*;

/** Абстрактный класс для команд */
public abstract class Command implements Serializable {
    protected final int argsCount;
    protected final String name;
    protected final String info;
    protected OutputWriter outputWriter;
    protected boolean scriptMode = false;
    protected InputReader inputReader;

    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param name
     * @param argsCount
     */
    public Command(String name, int argsCount, String info, InputReader inputReader) {
        this.name = name;
        this.argsCount = argsCount;
        this.info = info;
        this.inputReader = inputReader;
    }

    public void setInputReader(InputReader inputReader) {
        this.inputReader = inputReader;
    }

    public void setScriptMode(boolean scriptMode) {
        this.scriptMode = scriptMode;
    }

    /**
     * Выполнение логики команды
     *
     * @param args - аргументы
     * @throws IOException - ошибка чтения ввода
     * @throws CollectionIsEmptyException - коллекция пуста
     * @throws ExitCLIException - выход из CLI
     * @throws NoSuchKeyException - нет такого ключа в коллекции, на который пытается сослаться
     *     команда
     */
    public abstract void execute(String[] args)
            throws IOException,
                    CollectionIsEmptyException,
                    ExitCLIException,
                    NoSuchKeyException,
                    InvalidInputFieldException,
                    UserRejectedToFillFieldsException;

    /** Выдать справку об использовании команды */
    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    public int getArgsCount() {
        return argsCount;
    }

    /**
     * Задать вывод результата исполнения команды
     *
     * @see OutputWriter
     */
    public void setOutputWriter(OutputWriter newOutputWriter) {
        outputWriter = newOutputWriter;
    }
}
