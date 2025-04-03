package ru.askar.serverLab6.collectionCommand;

import java.io.IOException;
import ru.askar.common.exception.*;
import ru.askar.common.object.Command;
import ru.askar.common.object.Ticket;

public abstract class CollectionCommand extends Command {
    protected final boolean needObject;
    protected Ticket object;

    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param name
     * @param argsCount
     */
    public CollectionCommand(String name, int argsCount, String info, boolean needObject) {
        super(name, argsCount, info, null);
        this.needObject = needObject;
    }

    public boolean isNeedObject() {
        return needObject;
    }

    public Ticket getObject() {
        return object;
    }

    public void setObject(Ticket object) {
        this.object = object;
    }

    @Override
    public void execute(String[] args)
            throws IOException,
                    CollectionIsEmptyException,
                    ExitCLIException,
                    NoSuchKeyException,
                    InvalidInputFieldException,
                    UserRejectedToFillFieldsException {
        if (needObject && object == null) {
            throw new NoInputObjectException("В команду не предоставлен объект");
        }
    }
}
