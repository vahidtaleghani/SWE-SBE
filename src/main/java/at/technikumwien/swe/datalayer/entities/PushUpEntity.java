package at.technikumwien.swe.datalayer.entities;

import at.technikumwien.swe.datalayer.models.PushUpModel;
import at.technikumwien.swe.datalayer.models.TournamentState;

import java.util.Date;

public class PushUpEntity {
    private final int id;
    private final String username, workoutName;
    private final int amount, duration, tournamentState;
    private final Date addedTime;

    public PushUpEntity(int id, String username, String workoutName, int amount, int duration, Date addedTime, int tournamentState) {
        this.id = id;
        this.username = username;
        this.workoutName = workoutName;
        this.amount = amount;
        this.duration = duration;
        this.addedTime = addedTime;
        this.tournamentState = tournamentState;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public int getAmount() {
        return amount;
    }

    public int getDuration() {
        return duration;
    }

    public Date getAddedTime() {
        return addedTime;
    }

    public int getTournamentState() {
        return tournamentState;
    }

    public PushUpModel toModel() {
        return new PushUpModel(id, username, workoutName, amount, duration, addedTime, TournamentState.fromValue(tournamentState));
    }
}
