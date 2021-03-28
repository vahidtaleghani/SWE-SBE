package at.technikumwien.swe.datalayer.daos;

import at.technikumwien.swe.datalayer.Database;
import at.technikumwien.swe.datalayer.entities.PushUpEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PushUpDao {

    Connection connection = Database.getInstance().getConnection();

    public List<PushUpEntity> getAll(String username) {
        if (username == null) return null;

        String command = "SELECT id, username, workout_name, amount, duration FROM push_ups WHERE username = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(command);
            stmt.setString(1, username);

            ResultSet results = stmt.executeQuery();

            List<PushUpEntity> pushUpList = new LinkedList<>();
            while (results.next()) {
                pushUpList.add(pushUpRowToEntity(results));
            }
            return pushUpList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private PushUpEntity pushUpRowToEntity(ResultSet result) throws SQLException {
        return new PushUpEntity(
                result.getInt("id"),
                result.getString("username"),
                result.getString("workout_name"),
                result.getInt("amount"),
                result.getInt("duration")
        );
    }

    public boolean create(PushUpEntity pushUpEntity) {
        if (pushUpEntity == null) return false;

        String command = "INSERT INTO push_ups (username, workout_name, amount, duration) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(command);

            stmt.setString(1, pushUpEntity.getUsername());
            stmt.setString(2, pushUpEntity.getWorkoutName());
            stmt.setInt(3, pushUpEntity.getAmount());
            stmt.setInt(4, pushUpEntity.getDuration());

            stmt.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}