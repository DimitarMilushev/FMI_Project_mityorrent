package main.java.commands;

import main.java.application.Application;

import java.nio.channels.SocketChannel;

public abstract class NetworkCommand extends Command {
    protected SocketChannel channel;
    protected NetworkCommand(String name, Application application) {
        super(name, application);
    }

    public SocketChannel getChannel() {
        return this.channel;
    }

    public abstract void handleResponse() throws Exception;
}
