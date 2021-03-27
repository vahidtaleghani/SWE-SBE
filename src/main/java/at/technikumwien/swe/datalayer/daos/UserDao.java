package at.technikumwien.swe.datalayer.daos;

import at.technikumwien.swe.datalayer.Database;
import at.technikumwien.swe.datalayer.entities.UserEntity;

import java.sql.*;

public class UserDao {

    Connection connection = Database.getInstance().getConnection();

    public UserEntity getOne(String username) {
        if (username == null) return null;

        String command = "SELECT * FROM users WHERE username = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(command);
            stmt.setString(1, username);

            ResultSet results = stmt.executeQuery();

            if (results.next()) return userRowToEntity(results);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserEntity getOneByToken(String token) {
        if (token == null) return null;

        String command = "SELECT * FROM users WHERE token = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(command);
            stmt.setString(1, token);

            ResultSet results = stmt.executeQuery();

            if (results.next()) return userRowToEntity(results);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private UserEntity userRowToEntity(ResultSet result) throws SQLException {
        return new UserEntity(
                result.getString("username"),
                result.getString("password"),
                result.getString("token"),
                result.getString("name"),
                result.getString("biography"),
                result.getString("image")
        );
    }

    public boolean create(UserEntity userEntity) {
        if (userEntity == null) return false;
        String command = "INSERT INTO users (username, password, token, name, biography, image) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(command);
            stmt.setString(1, userEntity.getUsername());
            stmt.setString(2, userEntity.getPassword());
            stmt.setString(3, userEntity.getToken());

            if (userEntity.getName() == null) stmt.setNull(4, Types.VARCHAR);
            else stmt.setString(4, userEntity.getName());

            if (userEntity.getBiography() == null) stmt.setNull(5, Types.VARCHAR);
            else stmt.setString(5, userEntity.getBiography());

            if (userEntity.getImage() == null) stmt.setNull(6, Types.VARCHAR);
            else stmt.setString(6, userEntity.getImage());

            stmt.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}
