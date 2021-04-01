package at.technikumwien.swe.datalayer.models;

import at.technikumwien.swe.datalayer.entities.PushUpEntity;

import java.util.Date;

public class PushUpModel {
    private final int id;
    private final String username, workoutName;
    private final int amount, duration;
    private TournamentState tournamentState;
    private final Date addedTime;

    public PushUpModel(int id, String username, String workoutName, int amount, int duration, Date addedTime, TournamentState tournamentState) {
        this.id = id;
        this.username = username;
        this.workoutName = workoutName;
        this.amount = amount;
        this.duration = duration;
        this.addedTime = addedTime;
        this.tournamentState = tournamentState;
    }

    public PushUpModel(String username, String workoutName, int amount, int duration) {
        this(-1, username, workoutName, amount, duration, null, TournamentState.IN_PROGRESS);
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

    public TournamentState getTournamentState() {
        return tournamentState;
    }

    public void setTournamentState(TournamentState tournamentState) {
        this.tournamentState = tournamentState;
    }

    public PushUpEntity toEntity() {
        return new PushUpEntity(id, username, workoutName, amount, duration, addedTime, tournamentState.getValue());
    }

    @Override
    public String toString() {
        return "PushUpModel{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", workoutName='" + workoutName + '\'' +
                ", amount=" + amount +
                ", duration=" + duration +
                ", tournamentState=" + tournamentState +
                ", addedTime=" + addedTime +
                "}\n";
    }
}
