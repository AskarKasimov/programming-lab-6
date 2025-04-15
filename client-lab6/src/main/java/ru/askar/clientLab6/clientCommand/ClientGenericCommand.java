package ru.askar.clientLab6.clientCommand;

import java.io.IOException;
import ru.askar.clientLab6.connection.ClientHandler;
import ru.askar.common.CommandAsList;
import ru.askar.common.CommandResponse;
import ru.askar.common.CommandToExecute;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.exception.UserRejectedToFillFieldsException;
import ru.askar.common.object.Ticket;

public class ClientGenericCommand extends ClientCommand {
    private final InputReader inputReader;
    private final OutputWriter outputWriter;
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
        super(rowCommand.name(), rowCommand.args(), null, clientHandler);
        this.clientHandler = clientHandler;
        this.needObject = rowCommand.needObject();
        this.outputWriter = outputWriter;
        this.inputReader = inputReader;
    }

    @Override
    public CommandResponse execute(String[] args) {
        if (needObject) {
            String ticketName = args[1];
            long price;
            try {
                price = Long.parseLong(args[2]);
            } catch (NumberFormatException e) {
                return new CommandResponse(3, "В поле price требуется число");
            }

            Long id;
            if (args[0].equals("null")) {
                id = null;
            } else {
                id = Long.parseLong(args[0]);
            }
            try {
                Ticket ticket;
                ticket =
                        Ticket.createTicket(
                                outputWriter, inputReader, id, ticketName, price, null, scriptMode);
                clientHandler.sendMessage(new CommandToExecute(this.name, args, ticket));
            } catch (IOException
                    | InvalidInputFieldException
                    | UserRejectedToFillFieldsException e) {
                return new CommandResponse(3, e.getMessage());
            }
        } else {
            try {
                clientHandler.sendMessage(new CommandToExecute(this.name, args, null));
            } catch (IOException e) {
                return new CommandResponse(3, e.getMessage());
            }
        }
        return new CommandResponse(-1, "Команда отправлена на сервер");
    }
}
