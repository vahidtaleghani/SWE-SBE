package at.technikumwien.swe.datalayer.entities;

import at.technikumwien.swe.datalayer.models.UserModel;

public class UserEntity {
    private final String username, password;

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserModel toModel() {
        return new UserModel(username, password);
    }
}
