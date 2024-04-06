package server.messages;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageReader {
    private final SocketChannel channel;
    private final Map<SocketChannel, String> messageQueue;
    private final ByteBuffer buffer;
    private final List<byte[]> messages;

    public MessageReader(SocketChannel channel, Map<SocketChannel, String> messageQueue) {
        this.channel = channel;
        this.buffer = ByteBuffer.allocate(1024);
        this.messages = new LinkedList<>();
        this.messageQueue = messageQueue;
    }

    public void read() throws IOException {
        while (this.channel.read(this.buffer) > 0) {
            this.messages.add(this.buffer.array().clone());

            this.buffer.clear();
        }

        this.messageQueue.put(channel, this.yieldMessage());
    }

    /**
     * Collects the message parts and returns it, by clearing up the list.
     * @return String full message.
     */
    public String yieldMessage() {
        final String message
                = this.messages.stream().map((e) -> new String(e).trim()).collect(Collectors.joining());
        System.out.println("Added: " + message);
        this.messages.clear();
        return message;
    }
}
