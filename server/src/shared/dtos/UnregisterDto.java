package shared.dtos;

import java.util.Arrays;
import java.util.Collection;

public record UnregisterDto(String username, Collection<String> files) {
    public static UnregisterDto fromArgs(String[] args) {
        final String username = args[0];
        validateName(username);
        final Collection<String> filePaths = Arrays.stream(args).skip(1).toList();
        validateFiles(filePaths);

        return new UnregisterDto(username, filePaths);
    }

    private static void validateName(String username) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username is empty");
    }

    private static void validateFiles(Collection<String> files) {
        if (files == null || files.isEmpty()) throw new IllegalArgumentException("No files given");
    }
}
