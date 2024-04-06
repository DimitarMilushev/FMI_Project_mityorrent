package main.java.commands.jobs;

import main.java.SafeThread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class SendFileJob extends SafeThread {
    private final ByteBuffer buffer;
    private final SocketChannel channel;
    private final Path filePath;

    public SendFileJob(SocketChannel channel, Path filePath) {
        this.channel = channel;
        this.filePath = filePath;
        this.buffer = ByteBuffer.allocate(1024);
    }

    @Override
    public void run() {
        super.run();
        try (
                final var reader = Files.newByteChannel(filePath, StandardOpenOption.READ)
        ) {
            System.out.println("Sending " + filePath.getFileName() + " to " + channel.getRemoteAddress());
            while (reader.read(buffer) > 0) {
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
            }
            System.out.println(
                    "Successfully sent file " +
                            filePath.getFileName() +
                            " to " + channel.getRemoteAddress());
            System.out.println("Closing " + channel.getRemoteAddress() + "...");
            channel.close();
        } catch (IOException e) {
            System.out.println("Failed SendFileJob for " + filePath);
            e.printStackTrace();
            if (channel.isConnected()) {
                try {
                    final String message = "Error: Failed to send file " + e;
                    channel.write(ByteBuffer.wrap(message.getBytes()));
                } catch (IOException ex) {
                    System.out.println("Failed to print error message");
                    ex.printStackTrace();
                }
            }
        }
    }
}
