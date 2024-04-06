package main.java.commands;

import main.java.application.Application;

public abstract class Command {
    public final String name;
    protected final Application application;

    protected Command(String name, Application application) {
        this.name = name;
        this.application = application;
    }
    public abstract void execute() throws Exception;
}
