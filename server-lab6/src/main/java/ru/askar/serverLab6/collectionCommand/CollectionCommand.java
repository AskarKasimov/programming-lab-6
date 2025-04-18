package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.cli.Command;
import ru.askar.serverLab6.collection.CollectionManager;

public abstract class CollectionCommand extends Command {
    protected final CollectionManager collectionManager;

    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param name
     * @param argsCount
     */
    public CollectionCommand(
            String name, int argsCount, String info, CollectionManager collectionManager) {
        super(name, argsCount, info);
        this.collectionManager = collectionManager;
    }
}
