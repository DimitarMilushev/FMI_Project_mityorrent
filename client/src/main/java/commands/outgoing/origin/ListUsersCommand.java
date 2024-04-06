package main.java.commands.outgoing.origin;

import main.java.application.Application;
import main.java.commands.OriginChannelCommand;
import main.java.repository.UserRepository;
import main.java.repository.entities.User;

import java.io.NotSerializableException;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class ListUsersCommand extends OriginChannelCommand {
    public static final String NAME = "list-users";
    private final UserRepository repository;
    public ListUsersCommand(Application application, UserRepository repository) {
        super("ListUsersCommand", application);
        this.repository = repository;
    }

    @Override
    public void execute() throws Exception {
        System.out.println("Starting " + name + " command...");
        final ByteBuffer message = ByteBuffer.wrap(NAME.getBytes());
        channel.write(message);
        System.out.println("Completed " + name + " command...");
    }

    @Override
    public void handleResponse() throws Exception {
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        String message = "";
        while (channel.read(buffer) > 0) {
            buffer.flip();
            message += new String(buffer.array());
            buffer.clear();
        }
        message = message.trim();
        if (message.equals("NONE")) {
            System.out.println("No new records to update...");
            return;
        }
        final LinkedList<User> users = new LinkedList<>();
        final String[] outputLines = message.split("\n");
        String[] fields = new String[2];
        for(var line : outputLines) {
            fields = line.split(" - ");
            if (fields.length != 2) throw new NotSerializableException(line);
            users.push(new User(fields[0], fields[1]));
        }
        this.repository.rewrite(users.toArray(User[]::new));
    }
}
