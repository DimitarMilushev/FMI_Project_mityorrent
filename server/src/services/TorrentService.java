package services;

import shared.dtos.FileByUserResponse;
import shared.dtos.RegisterDto;
import data.TorrentRepository;
import data.entities.User;
import shared.dtos.UnregisterDto;

import java.util.LinkedList;

public class TorrentService {
    private final TorrentRepository repository;

    public TorrentService() {
        this.repository = TorrentRepository.getInstance();
    }

    public void registerTorrents(RegisterDto payload) throws Exception {
        final User user = new User(payload.user(), payload.address());
        if (!repository.userExists(user)) this.repository.addUser(user);

        this.repository.updateUserTorrents(user, payload.files());
    }

    public void unregisterTorrents(UnregisterDto payload) throws Exception {
        final User user = this.repository.getUserByName(payload.username());
        this.repository.removeUserTorrents(user, payload.files());
    }

    ;

    public void removeUserByAddress(String address) throws Exception {
        final User user = this.repository.getUserByAddress(address);
        if (user == null) throw new Exception("User with address " + address + " doesn't exist");
        this.repository.removeUser(user);
    }

    /**
     * Lists all torrents by usernames
     *
     * @return UserFilesResponse
     */
    public FileByUserResponse[] getAvailableFiles() {
        final var users = this.repository.getAllUsers();
        final LinkedList<FileByUserResponse> filesByUsers = new LinkedList<>();
        for (var user : users) {
            final String[] torrents = this.repository.getTorrentsByUser(user);
            for (var torrent : torrents) {
                filesByUsers.push(new FileByUserResponse(user.name(), torrent));
            }
        }
        return filesByUsers.toArray(FileByUserResponse[]::new);
    }

    public User[] getRegisteredUsers() {
        return this.repository.getAllUsers();
    }
}
