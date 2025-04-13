package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.object.Command;
import ru.askar.common.object.Ticket;

public abstract class CollectionCommand extends Command {
    protected final boolean needObject;
    protected Ticket ticket;

    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param name
     * @param argsCount
     */
    public CollectionCommand(
            String name,
            int argsCount,
            String info,
            OutputWriter outputWriter,
            boolean needObject) {
        super(name, argsCount, info, null, outputWriter);
        this.needObject = needObject;
    }

    public boolean isNeedObject() {
        return needObject;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
