package controllers;

import java.util.Arrays;

public class CommandProcessor {
    final Controller controller;
    public CommandProcessor() {
        this.controller = new Controller();
    }
    public String process(String input, String fromAddress) {
        final String[] tokens = input.split(" ");
        final Command command = Command.parseLabel(tokens[0]);
        switch (command) {
            case Command.Register -> {
                return this.controller.register(this.getArgs(tokens), fromAddress);
            }
            case Command.ListFiles -> {
                return this.controller.listFiles();
            }
            case Command.Unregister -> {
                return this.controller.unregister(this.getArgs(tokens));
            }
            case Command.ListUsers -> {
                return this.controller.getRegisteredUsers();
            }
            case null, default -> {
                return command + " is not supported.";
            }
        }
    }

    // Removes the command from the input
    private String[] getArgs(String[] tokens) {
        final String[] args = Arrays.stream(tokens).skip(1).toArray(String[]::new);
        clearCommaSeparators(args);

        return args;
    }

    // Removes comma characters from all tokens off the input
    private void clearCommaSeparators(String[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].replace(",", "");
        }
    }
}
