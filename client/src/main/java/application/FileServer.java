//package main.java.application;
//
//import main.java.application.types.QueuedCommand;
//import main.java.commands.Command;
//import main.java.commands.NetworkCommand;
//import main.java.commands.RemoteChannelCommand;
//import main.java.commands.outgoing.OutgoingCommandProcessor;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.ServerSocketChannel;
//import java.nio.channels.SocketChannel;
//import java.time.Instant;
//import java.util.*;
//import java.util.concurrent.ArrayBlockingQueue;
//
//public class FileServer {
//    final int port;
//    private ServerSocketChannel server;
//    private final Queue<RemoteChannelCommand> actions;
//    private final Map<SocketChannel, QueuedCommand> awaitedResponses;
//    public FileServer(int port) {
//        this.port = port;
//        this.actions = new ArrayBlockingQueue<>(20);
//        this.awaitedResponses = new HashMap<>();
//    }
//
//    public void run() {
//        try (
//                final var selector = Selector.open();
//                final var server = ServerSocketChannel.open();
//        ) {
//            server.bind(new InetSocketAddress(port));
//            server.configureBlocking(false);
//            server.register(selector, SelectionKey.OP_ACCEPT | SelectionKey.OP_CONNECT);
//
//            while(selector.isOpen()) {
//                int readyChannels = selector.selectNow();
//
//                if (readyChannels == 0) {
//                    Thread.sleep(2000);
//                    continue;
//                }
//                final Set<SelectionKey> keys = selector.selectedKeys();
//                final Iterator<SelectionKey> keysIterator = keys.iterator();
//                while (keysIterator.hasNext()) {
//                    final var currentKey = keysIterator.next();
//                    if (currentKey.isAcceptable()) handleAccept(currentKey);
//                    else if (currentKey.isConnectable()) handleConnected(currentKey);
//                    else if (currentKey.isReadable()) handleRead(currentKey);
//                    else if (currentKey.isWritable()) handleWrite(currentKey);
//                    keysIterator.remove();
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    // Successfully connected to a remote server
//    private void handleConnected(SelectionKey key) throws IOException {
//        System.out.println("isConnectable");
//        final SocketChannel client = (SocketChannel) key.channel();
//        if (!client.finishConnect()) {
//            System.out.println("Failed to connect to " + client.getRemoteAddress());
//            client.close();
//            return;
//        }
//        System.out.println("Successfully connected to " + client.getRemoteAddress());
//        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
//    }
//
//    private void handleAccept(SelectionKey key) throws IOException {
//        System.out.println("IsAcceptable");
//        final ServerSocketChannel server = (ServerSocketChannel) key.channel();
//        SocketChannel peer = server.accept();
//        if (peer == null) {
//            System.out.println("No incoming connection.");
//            return;
//        }
//        peer.configureBlocking(false);
//        peer.register(key.selector(), SelectionKey.OP_READ);
//
//        System.out.println("Connection Accepted: " + peer.getRemoteAddress() + "\n");
//    }
//
//    private void handleRead(SelectionKey key) throws Exception {
//        final SocketChannel channel = (SocketChannel) key.channel();
//        if (responseQueue.containsKey(channel)) {
//            final NetworkCommand action = responseQueue.get(channel).instance();
//            action.handleResponse();
//            responseQueue.remove(channel);
//        } else {
//            final ByteBuffer buffer = ByteBuffer.allocate(1024);
//            StringBuilder output = new StringBuilder();
//            while (channel.read(buffer) > 0) {
//                buffer.flip();
//                output.append(new String(buffer.array()));
//                buffer.clear();
//            }
//            final var message = output.toString().trim();
//            try {
//                final Command command = this.commandProcessor.process(message, channel);
//                this.setCommand(command);
//            } catch (UnsupportedOperationException ex) {
//                System.out.println("Failed to recognize incoming message " + ex);
//            }
//        }
//        key.interestOps(SelectionKey.OP_WRITE);
//    }
//
//    private void handleWrite(SelectionKey key) throws Exception {
//        final SocketChannel channel = (SocketChannel) key.channel();
//
//        final NetworkCommand action =
//                (this.getOriginChannel() == channel) ?
//                        this.originActions.poll() :
//                        this.clientActions.poll();
//        if (action == null) return;
//
//        action.execute();
//        responseQueue.put(action.getChannel(), new QueuedCommand(action, Date.from(Instant.now())));
////        if (channel.isConnected())
//        key.interestOps(SelectionKey.OP_READ);
//    }
//}
