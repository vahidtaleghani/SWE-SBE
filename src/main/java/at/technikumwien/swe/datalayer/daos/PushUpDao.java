package at.technikumwien.swe.datalayer.daos;

import at.technikumwien.swe.datalayer.Database;
import at.technikumwien.swe.datalayer.entities.PushUpEntity;
import at.technikumwien.swe.datalayer.entities.UserEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PushUpDao {

    Connection connection = Database.getInstance().getConnection();

    //--- getAll
    public List<PushUpEntity> getAll(String username) {
        if (username == null) return null;

        String command = "SELECT * FROM push_ups WHERE username = ?";

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

    //--- getAll
    public List<PushUpEntity> getAll(int tournamentState) {

        String command = "SELECT * FROM push_ups WHERE tournament_state = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(command);
            stmt.setInt(1, tournamentState);

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
                result.getInt("duration"),
                new Date(result.getTimestamp("added_time").getTime()),
                result.getInt("tournament_state")
        );
    }

    //--- getSum
    public int getSum(UserEntity userEntity) {
        if (userEntity == null) throw new RuntimeException("Cannot count from userEntity = null");

        String command = "SELECT SUM(amount) AS total FROM push_ups WHERE username = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(command);
            stmt.setString(1, userEntity.getUsername());

            ResultSet results = stmt.executeQuery();

            if (!results.next()) {
                throw new RuntimeException("No result retrieved from SUM()");
            }

            return results.getInt("total");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't query DB: " + e.getMessage());
        }
    }


    //---getCount
    public int getCount(UserEntity userEntity) {
        if (userEntity == null) throw new RuntimeException("Cannot count from userEntity = null");

        String command = "SELECT COUNT(*) AS entryCount FROM push_ups WHERE username = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(command);
            stmt.setString(1, userEntity.getUsername());

            ResultSet results = stmt.executeQuery();

            if (!results.next()) return 0;
            return results.getInt("entryCount");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't query DB: " + e.getMessage());
        }
    }

    //--- create
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

    //--- delete
    public boolean delete(UserEntity userEntity) {
        if (userEntity == null) return false;

        String command = "DELETE FROM push_ups where username= ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(command);
            stmt.setString(1, userEntity.getUsername());
            stmt.execute();

        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    //---update
    public boolean update(PushUpEntity pushUpEntity) {
        if (pushUpEntity == null) return false;
        String command = "UPDATE push_ups SET amount = ?, duration = ?, workout_name = ?, tournament_state = ? WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(command);

            stmt.setInt(1, pushUpEntity.getAmount());
            stmt.setInt(2, pushUpEntity.getDuration());
            stmt.setString(3, pushUpEntity.getWorkoutName());
            stmt.setInt(4, pushUpEntity.getTournamentState());

            stmt.setInt(5, pushUpEntity.getId());

            stmt.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}
