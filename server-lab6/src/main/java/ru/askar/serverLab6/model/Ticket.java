package ru.askar.serverLab6.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.exception.UserRejectedToFillFieldsException;

public class Ticket implements Comparable<Ticket>, Serializable {
    private LocalDateTime
            creationDate; // Поле не может быть null, Значение этого поля должно генерироваться
    // автоматически
    private Long
            id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля
    // должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private long price; // Значение поля должно быть больше 0
    private TicketType type; // Поле не может быть null
    private Event event; // Поле может быть null

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @JsonCreator
    private Ticket(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("coordinates") Coordinates coordinates,
            @JsonProperty("creationDate") LocalDateTime creationDate,
            @JsonProperty("price") long price,
            @JsonProperty("type") TicketType type,
            @JsonProperty("event") Event event)
            throws InvalidInputFieldException {
        setId(id);
        setName(name);
        setCoordinates(coordinates);
        setCreationDate(creationDate);
        setPrice(price);
        setType(type);
        setEvent(event);
    }

    private Ticket(Long ticketId, String name, long price) throws InvalidInputFieldException {
        setId(ticketId);
        setName(name);
        setPrice(price);
        this.creationDate = LocalDateTime.now();
    }

    /**
     * Создание экземпляра с пользовательским вводом. Параметры помимо <code>name</code> и <code>
     * price</code> будут считываться из заданного метода
     *
     * @param outputWriter - способ печати ответа
     * @param inputReader - способ считывания входных данных
     * @param ticketId - id билета
     * @param name - название
     * @param price - цена
     * @return - созданный Ticket
     */
    public static Ticket createTicket(
            OutputWriter outputWriter,
            InputReader inputReader,
            Long ticketId,
            String name,
            long price,
            Integer eventId,
            boolean scriptMode)
            throws InvalidInputFieldException, UserRejectedToFillFieldsException {
        Ticket ticket = new Ticket(ticketId, name, price);
        ticket.setCoordinates(Coordinates.createCoordinates(outputWriter, inputReader, scriptMode));
        ticket.setType(TicketType.createTicketType(outputWriter, inputReader, scriptMode));
        ticket.requestEvent(outputWriter, inputReader, eventId, scriptMode);
        return ticket;
    }

    private void requestEvent(
            OutputWriter outputWriter, InputReader inputReader, Integer eventId, boolean scriptMode)
            throws InvalidInputFieldException, UserRejectedToFillFieldsException {
        outputWriter.writeOnWarning("Хотите ввести событие? (y/n): ");
        String answer = inputReader.getInputString();
        if (answer != null && answer.equalsIgnoreCase("y")) {
            this.setEvent(Event.createEvent(outputWriter, inputReader, eventId, scriptMode));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return price == ticket.price
                && Objects.equals(id, ticket.id)
                && Objects.equals(name, ticket.name)
                && Objects.equals(coordinates, ticket.coordinates)
                && Objects.equals(creationDate, ticket.creationDate)
                && type == ticket.type
                && Objects.equals(event, ticket.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, price, type, event);
    }

    /** Сравнение, реализованное через разницу id'шников */
    @Override
    public int compareTo(Ticket other) {
        // Сначала сравниваем по типу билета
        int typeComparison = this.type.compareTo(other.type);
        if (typeComparison != 0) {
            return typeComparison;
        }

        // Если типы равны, сравниваем по цене
        return Long.compare(this.price, other.price);
    }

    @Override
    public String toString() {
        return "Билет"
                + ": id="
                + id
                + ", название='"
                + name
                + "'"
                + ", координаты="
                + coordinates
                + ", дата создания="
                + creationDate
                + ", цена="
                + price
                + ", тип="
                + type
                + ", событие="
                + event
                + ";";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) throws InvalidInputFieldException {
        if (id == null) {
            throw new InvalidInputFieldException("ID билета не может быть null");
        }
        if (id <= 0) {
            throw new InvalidInputFieldException("ID билета должен быть больше 0");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidInputFieldException {
        if (name == null || name.isEmpty()) {
            throw new InvalidInputFieldException("Название билета не может быть null или пустым");
        }
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) throws InvalidInputFieldException {
        if (coordinates == null) {
            throw new InvalidInputFieldException("Координаты билета не могут быть null");
        }
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) throws InvalidInputFieldException {
        if (price <= 0) {
            throw new InvalidInputFieldException("Цена билета должна быть больше 0");
        }
        this.price = price;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) throws InvalidInputFieldException {
        if (type == null) {
            throw new InvalidInputFieldException("Тип билета не может быть null");
        }
        this.type = type;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
