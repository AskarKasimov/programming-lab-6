package ru.askar.clientLab6.clientCommand;

import java.io.IOException;
import ru.askar.clientLab6.connection.ClientHandler;
import ru.askar.common.CommandAsList;
import ru.askar.common.CommandResponse;
import ru.askar.common.CommandToExecute;
import ru.askar.common.cli.CommandResponseCode;
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
            CommandAsList rawCommand,
            ClientHandler clientHandler,
            OutputWriter outputWriter) {
        super(rawCommand.name(), rawCommand.args(), null, clientHandler);
        this.clientHandler = clientHandler;
        this.needObject = rawCommand.needObject();
        this.outputWriter = outputWriter;
        this.inputReader = inputReader;
    }

    @Override
    public CommandResponse execute(String[] args) {
        if (needObject) {
            String ticketName = args[args.length - 2];
            long price;
            try {
                price = Long.parseLong(args[args.length - 1]);
            } catch (NumberFormatException e) {
                return new CommandResponse(
                        CommandResponseCode.ERROR, "В поле price требуется число (Long)");
            }

            Long id;
            if (args.length == 2) {
                id = null;
            } else if (args[args.length - 3].equalsIgnoreCase("null")) {
                id = null;
            } else {
                try {
                    id = Long.parseLong(args[0]);
                } catch (NumberFormatException e) {
                    return new CommandResponse(
                            CommandResponseCode.ERROR, "В поле id требуется число");
                }
            }
            try {
                Ticket ticket;
                ticket =
                        Ticket.createTicket(
                                outputWriter, inputReader, id, ticketName, price, null, scriptMode);
                clientHandler.sendMessage(new CommandToExecute(this.name, args, ticket));
            } catch (UserRejectedToFillFieldsException e) {
                return new CommandResponse(CommandResponseCode.ERROR, e.getMessage());
            }
        } else {
            clientHandler.sendMessage(new CommandToExecute(this.name, args, null));
        }
        return new CommandResponse(CommandResponseCode.HIDDEN, "Команда отправлена на сервер");
    }
}
