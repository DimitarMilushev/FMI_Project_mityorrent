package main.java.commands.incoming.client;

import main.java.application.Application;
import main.java.commands.RemoteChannelCommand;
import main.java.commands.jobs.SendFileJob;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

public class IncomingDownloadCommand extends RemoteChannelCommand {
    public static final String NAME = "download";
    private final Path filePath;
    public IncomingDownloadCommand(Application application, SocketChannel channel, String input) {
        super("IncomingDownloadCommand", application, channel);
        this.filePath = Path.of(input);
    }

    @Override
    public void execute() throws Exception {
        final SendFileJob job = new SendFileJob(channel, filePath);
        job.start();
    }

    @Override
    public void handleResponse() throws Exception {
//        final ByteBuffer buffer = ByteBuffer.allocate(1024);
//        StringBuilder response = new StringBuilder();
//        while (this.channel.read(buffer) > 0) {
//            buffer.flip();
//            response.append(new String(buffer.array()));
//            buffer.clear();
//        }
//        System.out.println(response.toString());
//        this.channel.close();
    }
}
