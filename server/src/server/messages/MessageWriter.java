package server.messages;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;

public class MessageWriter {
    private final SocketChannel channel;
    private final Map<SocketChannel, String> messageQueue;

    public MessageWriter(SocketChannel channel, Map<SocketChannel, String> messageQueue)
    {
        this.channel = channel;
        this.messageQueue = messageQueue;
    }

    public void write(String output) throws IOException {
        final ByteBuffer buffer = ByteBuffer.wrap(output.getBytes());
        this.channel.write(buffer);
    }

    public void write() throws IOException {
        final String message = messageQueue.get(channel);
        if (message == null) return;
        this.messageQueue.remove(channel);
        this.write(message);
    }
}
