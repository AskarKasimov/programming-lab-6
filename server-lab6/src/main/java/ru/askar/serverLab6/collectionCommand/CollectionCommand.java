package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.object.Command;

public abstract class CollectionCommand extends Command {
    protected final boolean needObject;

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
}
