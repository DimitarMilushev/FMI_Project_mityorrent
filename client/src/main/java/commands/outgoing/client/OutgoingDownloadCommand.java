package main.java.commands.outgoing.client;

import main.java.application.Application;
import main.java.commands.RemoteChannelCommand;
import main.java.commands.jobs.ReceiveFileJob;
import main.java.repository.UserRepository;
import main.java.repository.entities.User;
import main.java.repository.entities.UserField;
import main.java.utility.AddressUtility;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

public class OutgoingDownloadCommand extends RemoteChannelCommand {
    public static final String NAME = "download";
    private final String username;
    private final String remotePath;
    private final String destination;
    private final UserRepository userRepository;

    public OutgoingDownloadCommand(Application application, String input) throws Exception {
        super("OutgoingDownloadCommand", application);
        this.userRepository = UserRepository.getInstance();
        final var args = input.split(" ");
        this.username = args[0];
        this.remotePath = args[1];
        this.destination = args[2];
        this.connect(getAddress());
    }

    //download tosho C:\Users\Komp\Desktop\Gosho\Hello.txt C:\Users\Komp\Desktop\NewFile.txt
    @Override
    public void execute() throws Exception {
        System.out.println("Starting " + name + " command...");
        final String request = "download " + remotePath;
        final ByteBuffer message = ByteBuffer.wrap(request.getBytes());
        try (
                final SocketChannel downloadChannel = SocketChannel.open(getAddress());) {
            // Send request
            downloadChannel.write(message);
            // Await for data
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Date timeOut = Date.from(Instant.now().plus(15, ChronoUnit.SECONDS));
            while (downloadChannel.read(buffer) < 1) {
                Thread.sleep(100); //timeout
                if (Date.from(Instant.now()).after(timeOut)) {
                    System.out.println("Download request to " + username + " for " + request + " timed out...");
                    return;
                }
            }

            while (buffer.hasRemaining()) {

            }
        }
        System.out.println("Executed " + name);
    }

    @Override
    public void handleResponse() {
        System.out.println("Handling " + name + " response...");
        final ReceiveFileJob job = new ReceiveFileJob(channel, destination);
        job.start();
    }

    private InetSocketAddress getAddress() throws IOException {
        final User user = this.userRepository.findBy(UserField.name, username);
        return AddressUtility.fromString(user.address());
    }
}
