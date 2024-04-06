package main.java.commands;

import main.java.application.Application;

public abstract class CommandProcessor {
    protected final Application app;

    protected CommandProcessor(Application app) {
        this.app = app;
    }

    protected String withoutCommand(String input, String command) {
        return input.substring(command.length() + 1);
    }
}
