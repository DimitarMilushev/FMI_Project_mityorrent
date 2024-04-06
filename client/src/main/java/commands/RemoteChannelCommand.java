package main.java.commands;

import main.java.application.Application;
import main.java.commands.NetworkCommand;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public abstract class RemoteChannelCommand extends NetworkCommand {
    protected RemoteChannelCommand(String name, Application application) {
        super(name, application);
    }
    protected RemoteChannelCommand(String name, Application application, SocketChannel channel) {
        super(name, application);
        this.channel = channel;
    }

    protected void connect(InetSocketAddress address) throws Exception {
        if (channel != null && channel.isConnected())
            throw new Exception("Connection is already established with " + this.channel.getRemoteAddress());

        channel = this.application.connectToClient(address);
    }
}
