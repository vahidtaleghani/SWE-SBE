package at.technikumwien.swe;

import at.technikumwien.swe.datalayer.models.PushUpModel;
import at.technikumwien.swe.datalayer.models.TournamentState;
import at.technikumwien.swe.datalayer.models.UserEloModel;
import at.technikumwien.swe.datalayer.repositories.PushUpRepository;
import at.technikumwien.swe.datalayer.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PushUpsRepositoryTest {

    private final PushUpRepository pushUpRepository = new PushUpRepository();
    private final UserRepository userRepository = new UserRepository();

    @BeforeEach
    void initDatabase() throws SQLException {
        TestHelpers.resetDatabase();
        TestHelpers.insertUsers();
        TestHelpers.insertPushUps();
        TestHelpers.updatePushUpState();
    }

    @Test
    void addPushUps() {
        PushUpModel pushUpModel = new PushUpModel("unknownUser", "bh", 160, 50);
        assertFalse(pushUpRepository.addPushUp(pushUpModel));

        int before = pushUpRepository.getAll("user3").size();
        pushUpModel = new PushUpModel("user3", "bh", 160, 50);
        assertTrue(pushUpRepository.addPushUp(pushUpModel));
        assertEquals(before + 1, pushUpRepository.getAll("user3").size());
    }

    @Test
    void getAllByUserName() {
        assertNotNull(pushUpRepository.getAll("user3")); // Empty List but still a List

        PushUpModel pushUpModel = new PushUpModel("user3", "workoutTest", 160, 50);
        pushUpRepository.addPushUp(pushUpModel);
        pushUpRepository.addPushUp(pushUpModel);

        List<PushUpModel> pushUpModelList = pushUpRepository.getAll("user3");
        assertEquals(2, pushUpModelList.size());

        long count = pushUpModelList
                .stream()
                .filter(model -> model.getWorkoutName().equals("workoutTest"))
                .count();

        assertEquals(2, count);
    }

    @Test
    void getAllByTournamentState() {
        assertNotNull(pushUpRepository.getAll(TournamentState.IN_PROGRESS)); // Empty List but still a List

        PushUpModel pushUpModel1 = new PushUpModel("user3", "workoutTest", 160, 50);
        PushUpModel pushUpModel2 = new PushUpModel("user2", "workoutTest", 160, 50);
        pushUpRepository.addPushUp(pushUpModel1);
        pushUpRepository.addPushUp(pushUpModel2);

        List<PushUpModel> pushUpModelList = pushUpRepository.getAll(TournamentState.IN_PROGRESS);
        assertEquals(2, pushUpModelList.size());

        long count = pushUpModelList
                .stream()
                .filter(model -> model.getWorkoutName().equals("workoutTest"))
                .count();

        assertEquals(2, count);
    }

    @Test
    void updatePushUps() {
        PushUpModel pushUpModel = new PushUpModel("user3", "workoutTest1", 160, 30);
        pushUpRepository.addPushUp(pushUpModel);

        pushUpModel = pushUpRepository.getAll("user3")
                .stream()
                .filter(model -> model.getWorkoutName().equals("workoutTest1"))
                .findFirst().orElse(null);

        assertNotNull(pushUpModel);

        pushUpModel.setTournamentState(TournamentState.WIN);

        assertTrue(pushUpRepository.update(pushUpModel));

        pushUpModel = pushUpRepository.getAll("user3")
                .stream()
                .filter(model -> model.getWorkoutName().equals("workoutTest1"))
                .findFirst().orElse(null);

        assertNotNull(pushUpModel);
        assertEquals(TournamentState.WIN, pushUpModel.getTournamentState());
    }

    @Test
    void deletePushUps() {
        assertNotEquals(0, pushUpRepository.getAll("user1").size());
        assertTrue(pushUpRepository.delete(userRepository.getUser("user1")));
        assertEquals(0, pushUpRepository.getAll("user1").size());
    }

    @Test
    void getSumPushUps() {
        List<PushUpModel> pushUpModelList = pushUpRepository.getAll("user1");

        int sum = pushUpModelList.stream().mapToInt(PushUpModel::getAmount).sum();
        int repoSum = pushUpRepository.getSum(userRepository.getUser("user1"));

        assertEquals(sum, repoSum);
    }

    @Test
    void getCountPushUps() {
        List<PushUpModel> pushUpModelList = pushUpRepository.getAll("user1");
        assertEquals(pushUpModelList.size(), pushUpRepository.getCount(userRepository.getUser("user1")));
    }

    @Test
    void elo() {
        UserEloModel userElo1 = userRepository.getOneWithElo(userRepository.getUser("user1"));
        assertEquals(100 + 2 * 2, userElo1.getElo());

        UserEloModel userElo2 = userRepository.getOneWithElo(userRepository.getUser("user2"));
        assertEquals(100 + 2 * -1, userElo2.getElo());

        UserEloModel userElo3 = userRepository.getOneWithElo(userRepository.getUser("user3"));
        assertEquals(100, userElo3.getElo());
    }

    @Test
    void eloAll() {
        List<UserEloModel> userEloList = userRepository.getAllUsersWithElo();

        assertEquals(3, userEloList.size());

        for (UserEloModel userEloModel : userEloList) {
            switch (userEloModel.getUserModel().getUsername()) {
                case "user1" -> assertEquals(100 + 2 * 2, userEloModel.getElo());
                case "user2" -> assertEquals(100 + 2 * -1, userEloModel.getElo());
                case "user3" -> assertEquals(100, userEloModel.getElo());
            }
        }
    }
}
