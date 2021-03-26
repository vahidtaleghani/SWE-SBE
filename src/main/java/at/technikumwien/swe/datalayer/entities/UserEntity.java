package at.technikumwien.swe.datalayer.entities;

import at.technikumwien.swe.datalayer.models.UserModel;

public class UserEntity {
    private final String username, password, token;

    public UserEntity(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public UserModel toModel() {
        return new UserModel(username, password, token);
    }
}
