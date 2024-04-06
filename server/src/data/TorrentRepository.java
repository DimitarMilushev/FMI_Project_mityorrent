package data;

import data.entities.User;

import java.util.*;

public class TorrentRepository {
    // Unique address + path
    private final HashMap<User, LinkedList<String>> userTorrents;
    private static TorrentRepository instance;
    private TorrentRepository()  {
        userTorrents = new HashMap<>();
    }
    public static synchronized  TorrentRepository getInstance() {
        if (instance == null) {
            instance = new TorrentRepository();
        }
        return instance;
    }

    public boolean userExists(User user) {
     return this.userTorrents.containsKey(user);
    }

    public void addUser(User user) throws Exception {
        if (this.userTorrents.containsKey(user)) throw new Exception("User already exists");

        this.userTorrents.put(user, new LinkedList<>());
    }

    public void updateUserTorrents(User user, Collection<String> filePaths) throws Exception {
        if (user.equals(null)) throw new Exception("User " + user.name() + " doesn't exist.");

        final LinkedList<String> torrents = this.userTorrents.get(user);
        torrents.addAll(filePaths);
    }

    public User getUserByName(String username) {
        final var iterator = this.userTorrents.keySet().iterator();

        User user;
        while(iterator.hasNext()) {
            user = iterator.next();
            if (user.name().equals(username)) return user;
        }
        return null;
    }

    public User getUserByAddress(String address) {
        final var iterator = this.userTorrents.keySet().iterator();

        User user;
        while(iterator.hasNext()) {
            user = iterator.next();
            if (user.address().equals(address)) return user;
        }
        return null;
    }

    public void removeUserTorrents(User user, Collection<String> filePaths) throws Exception {
        final LinkedList<String> torrents = this.userTorrents.get(user);
        torrents.removeAll(filePaths);
    }

    public void removeUser(User user) {
        this.userTorrents.remove(user);
        System.out.println("Removed user " + user);
    }

    public User[] getAllUsers() {
        return this.userTorrents.keySet().toArray(User[]::new);
    }
    public String[] getTorrentsByUser(User user) {
        return this.userTorrents.get(user).toArray(String[]::new);
    }
}
