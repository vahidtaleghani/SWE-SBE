package at.technikumwien.swe.datalayer.daos;

import at.technikumwien.swe.datalayer.Database;
import at.technikumwien.swe.datalayer.entities.UserEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    Connection connection = Database.getInstance().getConnection();

    public UserEntity getOne(String username) {
        if (username == null) return null;

        String command = "SELECT * FROM users WHERE username = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(command);
            stmt.setString(1, username);

            ResultSet results = stmt.executeQuery();

            if (results.next()) return new UserEntity(
                    results.getString("username"),
                    results.getString("password"),
                    results.getString("token")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean create(UserEntity userEntity) {
        if (userEntity == null) return false;
        String command = "INSERT INTO users (username, password, token) VALUES (?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(command);
            stmt.setString(1, userEntity.getUsername());
            stmt.setString(2, userEntity.getPassword());
            stmt.setString(3, userEntity.getToken());

            stmt.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}
