package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.exception.*;
import ru.askar.common.object.Command;

import java.io.IOException;

public class CollectionCommand extends Command {
    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param name
     * @param argsCount
     */
    public CollectionCommand(String name, int argsCount) {
        super(name, argsCount, null);
    }

    @Override
    public void execute(String[] args) throws IOException, CollectionIsEmptyException, ExitCLIException, NoSuchKeyException, InvalidInputFieldException, UserRejectedToFillFieldsException {

    }

    @Override
    public String getInfo() {
        return "";
    }

    public Class<?> getClassForFilling() {
        return null;
    }
}
