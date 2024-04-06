package main.java.commands.outgoing;

import main.java.application.Application;
import main.java.commands.Command;
import main.java.commands.CommandProcessor;
import main.java.commands.outgoing.client.OutgoingDownloadCommand;
import main.java.commands.outgoing.origin.ListFilesCommand;
import main.java.commands.outgoing.origin.RegisterCommand;
import main.java.commands.outgoing.origin.UnregisterCommand;

public class OutgoingCommandProcessor extends CommandProcessor {
    public OutgoingCommandProcessor(Application application) {
        super(application);
    }

    public Command process(String input) throws Exception {
        if (input.isBlank()) throw new IllegalArgumentException("Empty input");
        final String[] tokens = input.split(" ");
        final String command = tokens[0];

        switch (command) {
            case RegisterCommand.NAME -> {
                return new RegisterCommand(app, this.withoutCommand(input, command));
            }
            case UnregisterCommand.NAME -> {
                return new UnregisterCommand(app, this.withoutCommand(input, command));
            }
            case ListFilesCommand.NAME -> {
                return new ListFilesCommand(app);
            }
            case OutgoingDownloadCommand.NAME -> {
                return new OutgoingDownloadCommand(app, this.withoutCommand(input, command));
            }
            default -> {
                throw new UnsupportedOperationException(command);
            }
        }
    }
}
