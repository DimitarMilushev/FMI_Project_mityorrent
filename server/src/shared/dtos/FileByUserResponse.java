package shared.dtos;

public record FileByUserResponse(String username, String filePath) {
    @Override
    public String toString() {
        return username + " : " + filePath;
    }
}
