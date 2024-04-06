package controllers;

import shared.dtos.RegisterDto;
import shared.dtos.FileByUserResponse;
import services.TorrentService;
import shared.dtos.UnregisterDto;

public class Controller {
    private final TorrentService service;

    public Controller() {
        this.service = new TorrentService();
    }

    /**
     * Registers torrent files by appending them to the respective username and address
     */
    public String register(String[] args, String address) {
        final var payload = RegisterDto.fromArgs(args, address);

        try {
            this.service.registerTorrents(payload);
            return "Success";
        } catch (Exception ex) {
            return "Failed: " + ex;
        }
    }

    public String listFiles() {
        final FileByUserResponse[] hostedFiles = this.service.getAvailableFiles();
        if (hostedFiles.length == 0) return "NONE";
        final StringBuilder sb = new StringBuilder();
        for (var hostedFile : hostedFiles) {
            sb.append(hostedFile.toString());
            sb.append("\n");
        }
        return sb.toString().stripTrailing();
    }

    public String unregister(String[] args) {
        final var payload = UnregisterDto.fromArgs(args);

        try {
            this.service.unregisterTorrents(payload);
            return "Success";
        } catch (Exception ex) {
            return "Failed: " + ex;
        }
    }

    public String getRegisteredUsers() {
        final var users = this.service.getRegisteredUsers();
        if (users.length == 0) return "NONE";

        final StringBuilder usersWithAddresses = new StringBuilder();
        for (var user : users) {
            usersWithAddresses
                    .append(user.name())
                    .append(" - ")
                    .append(user.address())
                    .append("\n");
        }

        return usersWithAddresses.toString().trim();
    }
}
