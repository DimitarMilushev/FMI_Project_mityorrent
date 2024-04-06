import server.Server;
import java.io.IOException;


public class Main {
    private static final int PORT = 3000;

    public static void main(String[] args) throws IOException, InterruptedException {
        final Server server = new Server(PORT);

        System.out.println("Starting server...");
        server.start();
    }
}