package shared.dtos;

import java.util.Arrays;
import java.util.Collection;

public record RegisterDto(String user, String address, Collection<String> files) {
    public static RegisterDto fromArgs(String[] args, String address) {
        validateAddress(address);
        final String username = args[0];
        validateName(username);
        final Collection<String> filePaths = Arrays.stream(args).skip(1).toList();
        validateFiles(filePaths);

        return new RegisterDto(username, address, filePaths);
    }

    private static void validateName(String username) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username is empty");
    }

    private static void validateAddress(String address) {
        if (address == null || address.isBlank()) throw new IllegalArgumentException("Address is empty");
        // TODO: Add schema check
    }

    private static void validateFiles(Collection<String> files) {
        if (files == null || files.isEmpty()) throw new IllegalArgumentException("No files given");
    }
}
