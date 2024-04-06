package main.java;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {
    private final Dotenv env;
    private static Env instance;
    public static synchronized Env instance() {
        if (instance == null) {
            instance = new Env();
        }
        return instance;
    }

    private Env() {
        env = Dotenv
                .configure()
                .directory("src/main/java/.env")
                .load();
    }

    public Dotenv getEnv() {
        return env;
    }
}
