package main.java;

public class SafeThread extends Thread {
    private boolean isRunning;

    /**
     * Marks thread as running
     */
    public void begin() {
        this.isRunning = true;
        System.out.println("Started " + this.getName() + " Job");
    }

    /**
     * Marks thread as stopped
     */
    public void end() {
        this.isRunning = false;
        System.out.println("Ended " + this.getName() + " Job");
    }

    @Override
    public void run() {
        super.run();
        this.begin();
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}
