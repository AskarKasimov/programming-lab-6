package ru.askar.serverLab6.collection;

import java.io.IOException;
import java.util.TreeMap;
import ru.askar.serverLab6.model.Ticket;

public interface DataReader {
    void readData() throws IOException;

    TreeMap<Long, Ticket> getData();

    String getSource();
}
