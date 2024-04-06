package main.java.repository;

import main.java.constants.EnvConstants;
import main.java.repository.entities.User;
import main.java.repository.entities.UserField;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;


public class UserRepository extends CSVRepository {
    private static UserRepository instance;

    private UserRepository() throws IOException {
        super(
                UserField.class,
                Path.of(EnvConstants.DATABASE_PATH,
                        "usersTable.csv").toFile());
    }

    public static UserRepository getInstance() throws IOException {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    /**
     * Completely re-writes the users base.
     */
    public synchronized void rewrite(User[] users) throws IOException {
        this.drop();
        this.addAll(users);
    }

    /**
     * Adds an array of users. Those who fail to get added are not part of the output.
     * No rollback method is implemented, so the process is completed regardless if any
     * of the operations fail.
     *
     * @return Array of added users
     */
    public synchronized User[] addAll(User[] users) {
        LinkedList<User> addedUsers = new LinkedList<>();
        for (var user : users) {
            try {
                addedUsers.add(this.add(user));
            } catch (Exception ex) {
                System.out.println("Failed to add " + user);
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return addedUsers.toArray(User[]::new);
    }

    /**
     * @param user record to be added
     * @return added record
     * @throws Exception if user already exists
     */
    public synchronized User add(User user) throws Exception {
        if (user == null) throw new Exception("Cannot user null values");
        if (this.hasRecord(UserField.name, user.name())) {
            throw new Exception("User with " + user.name() + " already exists");
        }
        try (
                final CSVPrinter printer = format.print(new FileWriter(tableFile, true))
        ) {
            printer.printRecord(user.name(), user.address());
            return user;
        }
    }

    public boolean hasRecord(UserField field, String value) throws IOException {
        return findBy(field, value) != null;
    }

    public synchronized User findBy(UserField field, String value) throws IOException {
        try (final Reader reader = new FileReader(tableFile)) {
            final List<CSVRecord> records = format.parse(reader).getRecords();
            User user = null;
            for (var record : records) {
                if (record.get(field.name()).equals(value)) {
                    user = new User(record.get(UserField.name), record.get(UserField.address));
                }
            }
            return user;
        }
    }
}

