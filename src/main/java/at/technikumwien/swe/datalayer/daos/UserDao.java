package at.technikumwien.swe.datalayer.daos;

import at.technikumwien.swe.datalayer.Database;
import at.technikumwien.swe.datalayer.entities.UserEloEntity;
import at.technikumwien.swe.datalayer.entities.UserEntity;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDao {

    Connection connection = Database.getInstance().getConnection();

    //--- getOne
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

    //--- getOneByToken
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

    //--- userRowToEntity
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

    //--- create
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

    //--- update
    public boolean update(UserEntity userEntity) {
        if (userEntity == null) return false;
        String command = "UPDATE users SET  password = ?, token = ?, name = ?, biography = ?, image = ? WHERE username = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(command);

            stmt.setString(1, userEntity.getPassword());
            stmt.setString(2, userEntity.getToken());

            if (userEntity.getName() == null) stmt.setNull(3, Types.VARCHAR);
            else stmt.setString(3, userEntity.getName());

            if (userEntity.getBiography() == null) stmt.setNull(4, Types.VARCHAR);
            else stmt.setString(4, userEntity.getBiography());

            if (userEntity.getImage() == null) stmt.setNull(5, Types.VARCHAR);
            else stmt.setString(5, userEntity.getImage());

            stmt.setString(6, userEntity.getUsername());

            stmt.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public UserEloEntity getOneWithElo(UserEntity userEntity) {
        //Gibt das Element an der angegebenen Position in dieser Liste zurück.
        return getAllWithElo(userEntity).get(0);
    }

    public List<UserEloEntity> getAllWithElo() {
        return getAllWithElo(null);
    }

    private List<UserEloEntity> getAllWithElo(UserEntity userEntity) {
        String whereCondition = ((userEntity != null) ? " WHERE u.username = ? " : " ");
        String command = """
                        SELECT (
                            100 +
                            -1 * coalesce(
                               (SELECT COUNT(*)
                                FROM push_ups p
                                WHERE p.username = u.username
                                  AND tournament_state = 2
                                GROUP BY p.username), 
                                0
                            )
                           +
                           2 * coalesce(
                               (SELECT COUNT(*)
                                FROM push_ups p
                                WHERE p.username = u.username
                                  AND tournament_state = 3
                                GROUP BY p.username),
                                0
                            )
                        ) AS elo,
                        u.*
                                
                        FROM users u
                """ + whereCondition + """
                        ORDER BY elo DESC
                ;
                """;


        try {
            PreparedStatement stmt = connection.prepareStatement(command);
            if (userEntity != null) {
                stmt.setString(1, userEntity.getUsername());
            }

            ResultSet results = stmt.executeQuery();

            List<UserEloEntity> userEloEntityList = new LinkedList<>();
            while (results.next()) {
                userEloEntityList.add(new UserEloEntity(
                        userRowToEntity(results),
                        results.getInt("elo")
                ));
            }

            return userEloEntityList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("couldnt find user! " + e.getMessage());
        }
    }
}
