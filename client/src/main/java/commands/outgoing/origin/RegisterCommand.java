package main.java.commands.outgoing.origin;

import main.java.application.Application;
import main.java.commands.OriginChannelCommand;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class RegisterCommand extends OriginChannelCommand {
    public static final String NAME = "register";
    private final String[] filePaths;

    public RegisterCommand(Application application, String input) {
        super("Register", application);
        this.filePaths = input.split(", ");
    }

    @Override
    public void execute() throws IOException {
        System.out.println("Starting " + name + " command...");
        // Check if each file exists
        this.validatePaths();
        String message = "register" +
                " " +
                application.username +
                " " +
                String.join(", ", filePaths);
        final ByteBuffer request = ByteBuffer.wrap(message.getBytes());
        this.channel.write(request);
        // Check response - OK | ERROR
        System.out.println("Completed " + name);
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

    private void validatePaths() throws FileNotFoundException {
        for (var file : this.filePaths) {
            final Path path = Path.of(file);
            if (Files.notExists(path)) throw new FileNotFoundException(file);
        }
    }
}
