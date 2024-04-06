package main.java.constants;

import main.java.Env;

public class EnvConstants {
    public static String ORIGIN_URI = Env.instance().getEnv().get("ORIGIN_URI");
    public static String DATABASE_PATH = Env.instance().getEnv().get("DATABASE_PATH");
}
