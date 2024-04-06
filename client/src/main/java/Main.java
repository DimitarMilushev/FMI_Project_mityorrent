package main.java;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import main.java.application.NonBlockingApplication;
import main.java.ui.UIJob;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println(Env.instance().getEnv().entries());
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Please, enter your name: ");
        final String username = scanner.nextLine();
        final var app = new NonBlockingApplication(username);

        final Thread UIThread = new UIJob(app);
        UIThread.start();
        // Server runs on the main thread
        app.start();
    }
}
