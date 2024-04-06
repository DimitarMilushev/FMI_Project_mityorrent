package main.java.application;

import main.java.application.jobs.RefreshUserBaseJob;
import main.java.application.types.QueuedCommand;
import main.java.commands.Command;
import main.java.commands.NetworkCommand;
import main.java.constants.EnvConstants;
import main.java.utility.AddressUtility;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class NonBlockingApplication extends Application {
    private final RefreshUserBaseJob refreshUserBaseJob;

    public NonBlockingApplication(String username) throws IOException {
        super(username, false, AddressUtility.fromString(EnvConstants.ORIGIN_URI));
        this.refreshUserBaseJob = new RefreshUserBaseJob(this);
    }

    @Override
    public void start() {
        try {
            this.selector = Selector.open();
            applicationChannel.register(selector, SelectionKey.OP_ACCEPT);
            originChannel.register(selector, SelectionKey.OP_WRITE);
            System.out.println("Listening on " + applicationChannel.getLocalAddress());
            applicationChannel.bind(originChannel.getLocalAddress());
            refreshUserBaseJob.start();
            while (selector.isOpen()) {
                int readyChannels = selector.selectNow();

                if (readyChannels == 0) {
                    Thread.sleep(2000);
                    continue;
                }
                final Set<SelectionKey> keys = selector.selectedKeys();
                final Iterator<SelectionKey> keysIterator = keys.iterator();
                while (keysIterator.hasNext()) {
                    final var currentKey = keysIterator.next();
                    if (currentKey.isAcceptable()) handleAccept(currentKey);
                    else if (currentKey.isConnectable()) handleConnected(currentKey);
                    else if (currentKey.isReadable()) handleRead(currentKey);
                    else if (currentKey.isWritable()) handleWrite(currentKey);
                    keysIterator.remove();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //TODO; close channels...
            refreshUserBaseJob.end();
        }
    }

    // Successfully connected to a remote server
    private void handleConnected(SelectionKey key) throws IOException {
        System.out.println("isConnectable");
        final SocketChannel client = (SocketChannel) key.channel();
        if (!client.finishConnect()) {
            System.out.println("Failed to connect to " + client.getRemoteAddress());
            client.close();
            return;
        }
        System.out.println("Successfully connected to " + client.getRemoteAddress());
        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    private void handleAccept(SelectionKey key) throws IOException {
        System.out.println("IsAcceptable");
        final ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel peer = server.accept();
        if (peer == null) {
            System.out.println("No incoming connection.");
            return;
        }
        peer.configureBlocking(false);
        peer.register(key.selector(), SelectionKey.OP_READ);

        System.out.println("Connection Accepted: " + peer.getRemoteAddress() + "\n");
    }

    private void handleRead(SelectionKey key) throws Exception {
        final SocketChannel channel = (SocketChannel) key.channel();
        if (responseQueue.containsKey(channel)) {
            final NetworkCommand action = responseQueue.get(channel).instance();
            action.handleResponse();
            responseQueue.remove(channel);
        } else {
            final ByteBuffer buffer = ByteBuffer.allocate(1024);
            StringBuilder output = new StringBuilder();
            while (channel.read(buffer) > 0) {
                buffer.flip();
                output.append(new String(buffer.array()));
                buffer.clear();
            }
            final var message = output.toString().trim();
            try {
                final Command command = this.commandProcessor.process(message, channel);
                this.setCommand(command);
            } catch (UnsupportedOperationException ex) {
                System.out.println("Failed to recognize incoming message " + ex);
            }
        }
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void handleWrite(SelectionKey key) throws Exception {
        final SocketChannel channel = (SocketChannel) key.channel();

        final NetworkCommand action =
                (this.getOriginChannel() == channel) ?
                        this.originActions.poll() :
                        this.clientActions.poll();
        if (action == null) return;

        action.execute();
        responseQueue.put(action.getChannel(), new QueuedCommand(action, Date.from(Instant.now())));
//        if (channel.isConnected())
        key.interestOps(SelectionKey.OP_READ);
    }
}
