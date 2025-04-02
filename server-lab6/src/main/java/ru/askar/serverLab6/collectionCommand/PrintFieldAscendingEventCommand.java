package ru.askar.serverLab6.collectionCommand;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;
import ru.askar.common.Command;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.object.Event;
import ru.askar.common.object.Ticket;
import ru.askar.serverLab6.collection.CollectionManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PrintFieldAscendingEventCommand extends Command {
    private final CollectionManager collectionManager;

    public PrintFieldAscendingEventCommand(CollectionManager collectionManager, InputReader inputReader) {
        super("print_field_ascending_event", 0, inputReader);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) {
        List<Event> eventList = collectionManager.getCollection().values().stream()
                .map(Ticket::getEvent)
                .filter(Objects::nonNull)
                .sorted().toList();
        outputWriter.writeln(AsciiTable.getTable(eventList, Arrays.asList(
                new Column().header("ID события").maxWidth(10).headerAlign(HorizontalAlign.CENTER).with(event -> String.valueOf(event.getId())),
                new Column().header("Название события").maxWidth(10).headerAlign(HorizontalAlign.CENTER).with(Event::getName),
                new Column().header("Описание события").maxWidth(20).headerAlign(HorizontalAlign.CENTER).with(Event::getDescription),
                new Column().header("Тип события").maxWidth(10).headerAlign(HorizontalAlign.CENTER).with(event -> event.getEventType() != null ? event.getEventType().name() : "-")
        )));
    }

    @Override
    public String getInfo() {
        return "print_field_ascending_event - вывести значения поля event всех элементов в порядке возрастания";
    }
}
