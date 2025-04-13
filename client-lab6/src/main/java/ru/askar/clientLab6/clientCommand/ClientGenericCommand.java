package ru.askar.clientLab6.clientCommand;

import java.io.IOException;
import ru.askar.clientLab6.connection.ClientHandler;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.dto.CommandAsList;
import ru.askar.common.dto.TransferredCommand;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.exception.UserRejectedToFillFieldsException;
import ru.askar.common.object.Ticket;

public class ClientGenericCommand extends ClientCommand {
    private final ClientHandler clientHandler;
    private final boolean needObject;

    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param inputReader
     */
    public ClientGenericCommand(
            InputReader inputReader, CommandAsList rowCommand, ClientHandler clientHandler) {
        super(rowCommand.name(), rowCommand.args(), rowCommand.info(), inputReader, clientHandler);
        this.clientHandler = clientHandler;
        this.needObject = rowCommand.needObject();
    }

    @Override
    public void execute(String[] args)
            throws InvalidInputFieldException, UserRejectedToFillFieldsException {
        if (needObject) {
            String name = args[1];
            long price;
            try {
                price = Long.parseLong(args[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("В поле price требуется число");
            }

            Long id;
            if (args[0].equals("null")) {
                id = null;
                outputWriter.writeOnWarning(
                        "id не был указан, поэтому он был сгенерирован автоматически (минимальный из отсутствующих): "
                                + id);
            } else {
                id = Long.parseLong(args[0]);
            }

            Ticket ticket =
                    Ticket.createTicket(
                            outputWriter, inputReader, id, name, price, null, scriptMode);
            try {
                clientHandler.sendMessage(new TransferredCommand(this.name, args, ticket));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                clientHandler.sendMessage(new TransferredCommand(name, args, null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
