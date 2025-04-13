package ru.askar.clientLab6.clientCommand;

import java.io.IOException;
import java.math.BigDecimal;
import ru.askar.clientLab6.connection.ClientHandler;
import ru.askar.common.CommandAsList;
import ru.askar.common.CommandToExecute;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.object.Coordinates;
import ru.askar.common.object.Ticket;
import ru.askar.common.object.TicketType;

public class ClientGenericCommand extends ClientCommand {
    private final ClientHandler clientHandler;
    private final boolean needObject;

    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param inputReader
     */
    public ClientGenericCommand(
            InputReader inputReader,
            CommandAsList rowCommand,
            ClientHandler clientHandler,
            OutputWriter outputWriter) {
        super(rowCommand.name(), rowCommand.args(), "", inputReader, clientHandler, outputWriter);
        this.clientHandler = clientHandler;
        this.needObject = rowCommand.needObject();
    }

    @Override
    public void execute(String[] args) throws InvalidInputFieldException {
        if (needObject) {
            try {
                clientHandler.sendMessage(
                        new CommandToExecute(
                                name,
                                args,
                                new Ticket(
                                        123L,
                                        "123",
                                        new Coordinates(123f, new BigDecimal(123)),
                                        123,
                                        TicketType.VIP,
                                        null)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                clientHandler.sendMessage(new CommandToExecute(name, args, null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
