package main.java.commands;

import main.java.application.Application;
import main.java.commands.NetworkCommand;

public abstract class OriginChannelCommand extends NetworkCommand {
    protected OriginChannelCommand(String name, Application application) {
        super(name, application);
        this.channel = application.getOriginChannel();
    }
}
