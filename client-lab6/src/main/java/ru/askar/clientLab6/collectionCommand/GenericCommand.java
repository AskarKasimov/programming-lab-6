package ru.askar.clientLab6.collectionCommand;

import java.io.IOException;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.exception.*;
import ru.askar.common.object.Command;

public class GenericCommand extends Command {

    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param name
     * @param argsCount
     * @param info
     * @param inputReader
     */
    public GenericCommand(String name, int argsCount, String info, InputReader inputReader) {
        super(name, argsCount, info, inputReader);
    }

    @Override
    public void execute(String[] args)
            throws IOException,
                    CollectionIsEmptyException,
                    ExitCLIException,
                    NoSuchKeyException,
                    InvalidInputFieldException,
                    UserRejectedToFillFieldsException {}
}
