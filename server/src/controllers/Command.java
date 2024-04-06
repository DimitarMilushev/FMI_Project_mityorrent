package controllers;

public enum Command {
    Register("register"),
    ListFiles("list-files"),
    Unregister("unregister"),
    ListUsers("list-users");

    public final String label;
    Command(String label) {
        this.label = label;
    }

    /**
     * Parses the string by matching it to a command label value.
     * @param value A string value that corresponds to the ENUM key, similar to a Map
     * @return Command or NULL if none match.
     */
    public static Command parseLabel(String value) {
        for (var command : Command.values()) {
            if (command.label.equals(value)) {
                return command;
            }
        }

        return null;
    }
}
