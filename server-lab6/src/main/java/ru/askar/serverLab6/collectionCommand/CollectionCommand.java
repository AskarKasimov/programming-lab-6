package ru.askar.serverLab6.collectionCommand;

import java.io.IOException;
import ru.askar.common.exception.*;
import ru.askar.common.object.Command;

public class CollectionCommand extends Command {
    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param name
     * @param argsCount
     */
    public CollectionCommand(String name, int argsCount, String info) {
        super(name, argsCount, info, null);
    }

    @Override
    public void execute(String[] args)
            throws IOException,
                    CollectionIsEmptyException,
                    ExitCLIException,
                    NoSuchKeyException,
                    InvalidInputFieldException,
                    UserRejectedToFillFieldsException {}

    public Class<?> getClassForFilling() {
        return null;
    }
}
