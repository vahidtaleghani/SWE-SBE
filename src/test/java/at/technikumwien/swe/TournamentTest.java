package at.technikumwien.swe;

import at.technikumwien.swe.datalayer.models.PushUpModel;
import at.technikumwien.swe.datalayer.models.TournamentState;
import at.technikumwien.swe.datalayer.repositories.PushUpRepository;
import at.technikumwien.swe.datalayer.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TournamentTest {

    private final PushUpRepository pushUpRepository = new PushUpRepository();
    private final UserRepository userRepository = new UserRepository();

    @BeforeEach
    void initDatabase() throws SQLException {
        TestHelpers.resetDatabase();
        TestHelpers.insertUsers();
        TestHelpers.insertPushUps();
    }

    @Test
    void tournamentOnlyOneWinner() throws InterruptedException {
        Thread.sleep(1200);
        TournamentController.handle(1);

        assertEquals(0, pushUpRepository.getAll(TournamentState.IN_PROGRESS).size());
    }

    @Test
    void correctWinner() throws InterruptedException {
        Thread.sleep(1200);
        TournamentController.handle(1);

        assertEquals("user1", pushUpRepository.getAll(TournamentState.WIN).get(0).getUsername());
    }

    @Test
    void correctLooser() throws InterruptedException {
        Thread.sleep(1200);
        TournamentController.handle(1);

        assertEquals("user2", pushUpRepository.getAll(TournamentState.LOSE).get(0).getUsername());
    }

    @Test
    void winCountsOnlyOnce() throws InterruptedException {
        Thread.sleep(1200);
        TournamentController.handle(1);

        assertEquals(2, pushUpRepository.getAll(TournamentState.FINISHED_BUT_IGNORED).size());
    }

    @Test
    void multipleTournaments() throws InterruptedException {
        Thread.sleep(1100);

        pushUpRepository.addPushUp(new PushUpModel("user3", "Training", 10, 20));
        pushUpRepository.addPushUp(new PushUpModel("user3", "Training", 20, 30));
        pushUpRepository.addPushUp(new PushUpModel("user1", "Workout", 15, 40));
        pushUpRepository.addPushUp(new PushUpModel("user2", "Workout", 10, 60));

        Thread.sleep(1100);

        TournamentController.handle(1);

        assertEquals(2, pushUpRepository.getAll(TournamentState.WIN).size());
        assertEquals(3, pushUpRepository.getAll(TournamentState.LOSE).size());
        assertEquals(3, pushUpRepository.getAll(TournamentState.FINISHED_BUT_IGNORED).size());
    }

}
