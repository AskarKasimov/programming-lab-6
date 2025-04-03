package ru.askar.serverLab6.collectionCommand;

import java.io.IOException;
import ru.askar.common.exception.*;

public class ExitCommand extends CollectionCommand {
    public ExitCommand() {
        super("exit", 0, "exit - завершить программу (без сохранения в файл)", false);
    }

    @Override
    public void execute(String[] args)
            throws ExitCLIException,
                    NoSuchKeyException,
                    InvalidInputFieldException,
                    CollectionIsEmptyException,
                    IOException,
                    UserRejectedToFillFieldsException {
        super.execute(args);
        throw new ExitCLIException();
    }
}
