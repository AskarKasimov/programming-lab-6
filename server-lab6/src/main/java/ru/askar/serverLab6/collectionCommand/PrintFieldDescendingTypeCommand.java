package ru.askar.serverLab6.collectionCommand;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;
import ru.askar.common.object.Command;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.object.Ticket;
import ru.askar.common.object.TicketType;
import ru.askar.serverLab6.collection.CollectionManager;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PrintFieldDescendingTypeCommand extends Command {
    private final CollectionManager collectionManager;

    public PrintFieldDescendingTypeCommand(CollectionManager collectionManager, InputReader inputReader) {
        super("print_field_descending_type", 0, inputReader);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) {
        List<TicketType> ticketTypes = collectionManager.getCollection().values().stream()
                .map(Ticket::getType)
                .filter(Objects::nonNull)
                .sorted().toList();
        outputWriter.writeln(AsciiTable.getTable(ticketTypes, Collections.singletonList(
                new Column().header("Тип").maxWidth(10).headerAlign(HorizontalAlign.CENTER).with(Enum::name)
        )));
    }

    @Override
    public String getInfo() {
        return "print_field_descending_type - вывести значения поля type всех элементов в порядке убывания";
    }
}
