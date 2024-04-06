package main.java.application.jobs;

import main.java.application.Application;
import main.java.commands.outgoing.origin.ListUsersCommand;
import main.java.repository.UserRepository;
import main.java.SafeThread;

import java.io.IOException;
import java.time.Duration;

public class RefreshUserBaseJob extends SafeThread {
    private final Application app;
    private final UserRepository repository;
    public RefreshUserBaseJob(Application app) throws IOException {
        this.app = app;
        this.repository = UserRepository.getInstance();
        this.setName("RefreshUserBase");
    }

    @Override
    public void run() {
        super.run();
        while(isRunning() && isAlive()) {
            app.setCommand(new ListUsersCommand(app, repository));
            try {
                Thread.sleep(Duration.ofSeconds(30)); // 30 seconds
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                System.out.println(getName() + " was interrupted: " + ex);
                this.end();
            }
        }
        this.end();
    }
}
