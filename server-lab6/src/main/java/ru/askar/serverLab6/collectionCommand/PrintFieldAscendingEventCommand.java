package ru.askar.serverLab6.collectionCommand;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import ru.askar.common.CommandResponse;
import ru.askar.common.cli.CommandResponseCode;
import ru.askar.common.object.Event;
import ru.askar.common.object.Ticket;
import ru.askar.serverLab6.collection.CollectionManager;

public class PrintFieldAscendingEventCommand extends CollectionCommand {
    public PrintFieldAscendingEventCommand(CollectionManager collectionManager) {
        super(
                "print_field_ascending_event",
                0,
                "print_field_ascending_event - вывести значения поля event всех элементов в порядке возрастания",
                collectionManager);
    }

    @Override
    public CommandResponse execute(String[] args) {
        List<Event> eventList =
                collectionManager.getCollection().values().stream()
                        .map(Ticket::getEvent)
                        .filter(Objects::nonNull)
                        .sorted()
                        .toList();
        return new CommandResponse(
                CommandResponseCode.INFO,
                AsciiTable.getTable(
                        eventList,
                        Arrays.asList(
                                new Column()
                                        .header("ID события")
                                        .maxWidth(10)
                                        .headerAlign(HorizontalAlign.CENTER)
                                        .with(event -> String.valueOf(event.getId())),
                                new Column()
                                        .header("Название события")
                                        .maxWidth(10)
                                        .headerAlign(HorizontalAlign.CENTER)
                                        .with(Event::getName),
                                new Column()
                                        .header("Описание события")
                                        .maxWidth(20)
                                        .headerAlign(HorizontalAlign.CENTER)
                                        .with(Event::getDescription),
                                new Column()
                                        .header("Тип события")
                                        .maxWidth(10)
                                        .headerAlign(HorizontalAlign.CENTER)
                                        .with(
                                                event ->
                                                        event.getEventType() != null
                                                                ? event.getEventType().name()
                                                                : "-"))));
    }
}
