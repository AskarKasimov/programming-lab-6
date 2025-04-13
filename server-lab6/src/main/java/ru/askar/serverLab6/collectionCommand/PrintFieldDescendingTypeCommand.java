package ru.askar.serverLab6.collectionCommand;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import ru.askar.common.exception.*;
import ru.askar.serverLab6.collection.CollectionManager;
import ru.askar.serverLab6.model.Ticket;
import ru.askar.serverLab6.model.TicketType;

public class PrintFieldDescendingTypeCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public PrintFieldDescendingTypeCommand(CollectionManager collectionManager) {
        super(
                "print_field_descending_type",
                0,
                "print_field_descending_type - вывести значения поля type всех элементов в порядке убывания",
                false);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args)
            throws NoSuchKeyException,
                    InvalidInputFieldException,
                    CollectionIsEmptyException,
                    ExitCLIException,
                    IOException,
                    UserRejectedToFillFieldsException {
        super.execute(args);
        List<TicketType> ticketTypes =
                collectionManager.getCollection().values().stream()
                        .map(Ticket::getType)
                        .filter(Objects::nonNull)
                        .sorted()
                        .toList();
        outputWriter.writeln(
                AsciiTable.getTable(
                        ticketTypes,
                        Collections.singletonList(
                                new Column()
                                        .header("Тип")
                                        .maxWidth(10)
                                        .headerAlign(HorizontalAlign.CENTER)
                                        .with(Enum::name))));
    }
}
