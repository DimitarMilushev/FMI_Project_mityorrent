package server;

import com.sun.jdi.connect.spi.ClosedConnectionException;
import controllers.CommandProcessor;
import server.messages.MessageReader;
import server.messages.MessageWriter;
import services.TorrentService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.*;
import java.util.*;

public class Server {
    private final int port;
    private final Map<SelectableChannel, SocketAddress> activeSessions;
    private final TorrentService torrentService;
    Map<SocketChannel, String> requestQueue;
    Map<SocketChannel, String> responseQueue;
    final CommandProcessor commandProcessor = new CommandProcessor();

    public Server(int port) {
        this.port = port;
        torrentService = new TorrentService();
        activeSessions = new HashMap<>();

        responseQueue = new HashMap<>();
        requestQueue = new HashMap<>();
    }

    public void start() throws IOException, InterruptedException {
        try (
                final Selector selector = Selector.open();
                final ServerSocketChannel serverChannel =
                        ServerSocketChannel.open()
                                .bind(new InetSocketAddress(port));
        ) {
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Awaiting connection on " + serverChannel.getLocalAddress());
            while (selector.isOpen()) {
                int readyChannels = selector.selectNow();
                if (readyChannels == 0) {
                    Thread.sleep(2000);
                    continue;
                }
                System.out.println(readyChannels + " channels are ready...");

                final Set<SelectionKey> keys = selector.selectedKeys();
                final Iterator<SelectionKey> keysIterator = keys.iterator();

                while (keysIterator.hasNext()) {

                    final var currentKey = keysIterator.next();
                    if (currentKey.isAcceptable()) handleAccept(currentKey, selector);
                    else if (currentKey.isReadable()) handleRead(currentKey);
                    else if (currentKey.isWritable()) handleWrite(currentKey);
                    keysIterator.remove();
                }

                if (!this.requestQueue.isEmpty()) {
                    this.requestQueue.forEach(this::processCommand);
                }
            }
        }

    }

    private void processCommand(SocketChannel channel, String input) {
        try {
            final String response = commandProcessor.process(input, channel.getRemoteAddress().toString());
            this.responseQueue.put(channel, response);
        } catch (IOException ex) {
            System.out.println("Failed to process command " + ex);
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        final var channel = (SocketChannel) key.channel();
        try {
            new MessageReader(channel, requestQueue).read();
            key.interestOps(SelectionKey.OP_WRITE);
        } catch (SocketException ex) {
            System.out.println("Failed to read message from " + channel.getRemoteAddress());
            System.out.println("Error: " + ex);
            this.handleCloseChannel(key);
        }
    }

    private void handleWrite(SelectionKey key) throws IOException {
        final var channel = (SocketChannel) key.channel();
        try {
            new MessageWriter(channel, responseQueue).write();
            key.interestOps(SelectionKey.OP_READ);
        } catch (SocketException ex) {
            System.out.println("Connection lost: " + channel.getRemoteAddress());
            System.out.println("Error: " + ex);
            this.handleCloseChannel(key);
        }
    }

    private void handleCloseChannel(SelectionKey key) throws IOException {
        key.cancel();
        key.channel().close();
        this.handleClosed(key);
    }

    private void handleClosed(SelectionKey key) {
        this.requestQueue.remove(key.channel());
        this.responseQueue.remove(key.channel());
        final SocketAddress address = this.activeSessions.get(key.channel());
        if (address == null) return;
        System.out.println("Channel " + address + " is closed.");
        try {
            System.out.println("Removing user with address " + address);
            torrentService.removeUserByAddress(address.toString());
        } catch (Exception ex) {
            System.out.println("Failed to remove user " + address + " " + ex);
        }
    }

    private void handleAccept(SelectionKey key, Selector selector) throws IOException {
        final ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        System.out.println("IsAcceptable");

        SocketChannel client = serverSocketChannel.accept();
        if (client == null) {
            System.out.println("No incoming connection.");
            return;
        }
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        this.activeSessions.put(client, client.getRemoteAddress());
        System.out.println("Connection Accepted: " + client.getRemoteAddress() + "\n");
    }
}
