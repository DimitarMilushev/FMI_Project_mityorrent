package main.java.commands.outgoing.origin;

import main.java.application.Application;
import main.java.commands.OriginChannelCommand;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class ListFilesCommand extends OriginChannelCommand {
    public static final String NAME = "list-files";
    public ListFilesCommand(Application application) {
        super("ListFiles", application);
    }

    @Override
    public void execute() throws Exception {
        System.out.println("Starting " + name + " command...");
        final ByteBuffer message = ByteBuffer.wrap("list-files".getBytes());
        channel.write(message);
        System.out.println("Executed " + name);
    }

    @Override
    public void handleResponse() throws Exception {
        System.out.println("Handling " + name + " response...");
        LinkedList<byte[]> responseBytes = new LinkedList<>();
        final var buffer = ByteBuffer.allocate(1024);

        while (channel.read(buffer) > 0) {
            responseBytes.add(buffer.array());
            buffer.clear();
        }
        final var message = responseBytes.stream().map((x) -> new String(x).trim()).collect(Collectors.joining());
        System.out.println(message);
    }
}
