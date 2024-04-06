package main.java.ui;

import main.java.application.Application;
import main.java.commands.Command;
import main.java.commands.outgoing.OutgoingCommandProcessor;

import java.io.IOException;
import java.util.Scanner;

public class UIJob extends Thread {
    private final Application application;
    private boolean isRunning;
    private final OutgoingCommandProcessor commandProcessor;
    public UIJob(Application application) {
        this.application = application;
        this.commandProcessor = new OutgoingCommandProcessor(application);
    }
    @Override
    public void run() {
        super.run();
        this.isRunning = true;

        final Scanner scanner = new Scanner(System.in);
        while(isRunning && isAlive()) {
            final String input = scanner.nextLine();
            if (input.isBlank()) continue;
            final Command command;
            try {
                command = this.commandProcessor.process(input);
            } catch (UnsupportedOperationException ex) {
                System.out.println("Failed on input " + input);
                System.out.println(ex);
                continue;
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            //TODO: change

            final var accepted = this.application.setCommand(command);
            System.out.println(command + " was accepted: " + accepted);
        }
    }

    public void end() {
        this.isRunning = false;
    }

}
