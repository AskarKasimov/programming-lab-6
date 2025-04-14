package ru.askar.serverLab6.collectionCommand;

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
        super(name, argsCount, info);
        this.needObject = needObject;
    }

    public boolean isNeedObject() {
        return needObject;
    }

    public void setObject(Ticket object) {
        this.object = object;
    }
}
