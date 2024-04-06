package main.java.commands.jobs;

import main.java.SafeThread;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ReceiveFileJob extends SafeThread {
    private final ByteBuffer buffer;
    private final SocketChannel channel;
    private final Path destination;

    public ReceiveFileJob(SocketChannel channel, String destination) {
        this.channel = channel;
        this.destination = Path.of(destination);
        buffer = ByteBuffer.allocate(1024);
    }

    @Override
    public void run() {
        super.run();
        System.out.println("Started ReceiveFileJob for file " + destination);
        SocketAddress remoteAddress = null;
        try (
                final BufferedWriter fileWriter = Files.newBufferedWriter(
                        destination,
                        StandardOpenOption.CREATE_NEW,
                        StandardOpenOption.APPEND
                );
        ) {
            remoteAddress = channel.getRemoteAddress();
            while (channel.read(buffer) > 0) {
                buffer.flip();
                fileWriter.write(new String(buffer.array()));

                buffer.clear();
            }
            System.out.println("Finished ReceiveFileJob for file " + destination);
        } catch (IOException e) {
            System.out.println("Failed receive file job for " + destination + " from " + remoteAddress);
        }
    }
}
