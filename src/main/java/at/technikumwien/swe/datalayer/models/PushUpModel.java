package at.technikumwien.swe.datalayer.models;

import at.technikumwien.swe.datalayer.entities.PushUpEntity;

public class PushUpModel {
    private final int id;
    private final String username, workoutName;
    private final int amount, duration;
    private TournamentState tournamentState;

    public PushUpModel(int id, String username, String workoutName, int amount, int duration, TournamentState tournamentState) {
        this.id = id;
        this.username = username;
        this.workoutName = workoutName;
        this.amount = amount;
        this.duration = duration;
        this.tournamentState = tournamentState;
    }

    public PushUpModel(String username, String workoutName, int amount, int duration) {
        this(-1, username, workoutName, amount, duration, TournamentState.IN_PROGRESS);
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

    public TournamentState getTournamentState() {
        return tournamentState;
    }

    public PushUpEntity toEntity() {
        return new PushUpEntity(id, username, workoutName, amount, duration, tournamentState.getValue());
    }
}
