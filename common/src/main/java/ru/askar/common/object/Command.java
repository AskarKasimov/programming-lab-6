package ru.askar.common.object;

import java.io.Serializable;
import ru.askar.common.CommandResponse;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.ExitCLIException;

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
    public Command(
            String name,
            int argsCount,
            String info,
            InputReader inputReader,
            OutputWriter outputWriter) {
        this.name = name;
        this.argsCount = argsCount;
        this.info = info;
        this.inputReader = inputReader;
        this.outputWriter = outputWriter;
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
     * @throws ExitCLIException - выход из CLI команда
     */
    public abstract CommandResponse execute(String[] args) throws ExitCLIException;

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
