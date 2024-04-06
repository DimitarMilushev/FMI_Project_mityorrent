package main.java.commands.incoming;

import main.java.application.Application;
import main.java.commands.Command;
import main.java.commands.CommandProcessor;
import main.java.commands.incoming.client.IncomingDownloadCommand;

import java.nio.channels.SocketChannel;

public class IncomingCommandProcessor extends CommandProcessor {
    public IncomingCommandProcessor(Application app) {
        super(app);
    }

    public Command process(String input, SocketChannel channel) throws Exception {
        // TODO: Add headers instead after implementing a protocol
        if (input.isBlank()) throw new IllegalArgumentException("Empty input");
        final String[] tokens = input.split(" ");
        final String command = tokens[0];

        switch (command) {
            case IncomingDownloadCommand.NAME -> {
                return new IncomingDownloadCommand(app, channel, this.withoutCommand(input, command));
            }
            default -> throw new UnsupportedOperationException(command);
        }
    }
}
