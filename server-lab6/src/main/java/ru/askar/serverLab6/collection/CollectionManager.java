package ru.askar.serverLab6.collection;

import com.fasterxml.jackson.databind.JsonMappingException;
import ru.askar.common.exception.InvalidCollectionFileException;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.object.Event;
import ru.askar.common.object.Ticket;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Manager для коллекции билетов.
 */
public class CollectionManager {
    private final LocalDateTime dateOfInitialization;
    private final TreeMap<Long, Ticket> collection;
    private final DataReader starterDataReader;

    public CollectionManager(DataReader dataReader) throws InvalidInputFieldException, IOException {
        this.dateOfInitialization = LocalDateTime.now();
        if (dataReader == null) {
            this.starterDataReader = new DataReader() {
                @Override
                public void readData() throws IOException {
                }

                @Override
                public TreeMap<Long, Ticket> getData() {
                    return new TreeMap<>();
                }

                @Override
                public String getSource() {
                    return null;
                }
            };
        } else this.starterDataReader = dataReader;
        try {
            this.starterDataReader.readData();
        } catch (JsonMappingException e) {
            Throwable cause = e.getCause();
            if (cause != null) {
                throw new InvalidInputFieldException("Критическая ошибка поля структуры: " + cause.getMessage());
            } else {
                throw new IOException("Неизвестная ошибка считывания данных из файла: " + e.getOriginalMessage());
            }
        } catch (InvalidCollectionFileException e) {
            throw new InvalidCollectionFileException("Критическая ошибка читаемого файла: " + e.getMessage());
        } catch (IOException e) {
            throw new IOException("Ошибка при чтении файла: " + e.getMessage());
        }
        this.collection = this.starterDataReader.getData();
    }

    public String getStarterSource() {
        return starterDataReader.getSource();
    }

    public Long generateNextTicketId() {
        long min = 1;
        while (collection.containsKey(min)) {
            min++;
        }
        return min;
    }

    public Integer generateNextEventId() {
        Set<Integer> ids = collection.values().stream()
                .map(Ticket::getEvent)
                .filter(Objects::nonNull)
                .map(Event::getId)
                .collect(Collectors.toSet());

        int min = 1;
        while (ids.contains(min)) {
            min++;
        }
        return min;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfInitialization;
    }

    public TreeMap<Long, Ticket> getCollection() {
        return collection;
    }
}