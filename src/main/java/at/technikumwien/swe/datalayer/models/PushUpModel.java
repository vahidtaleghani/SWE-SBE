package at.technikumwien.swe.datalayer.models;

import at.technikumwien.swe.datalayer.entities.PushUpEntity;

public class PushUpModel {
    private final int id;
    private final String username, workoutName;
    private final int amount, duration;

    public PushUpModel(int id, String username, String workoutName, int amount, int duration) {
        this.id = id;
        this.username = username;
        this.workoutName = workoutName;
        this.amount = amount;
        this.duration = duration;
    }

    public PushUpModel(String username, String workoutName, int amount, int duration) {
        this.id = -1;
        this.username = username;
        this.workoutName = workoutName;
        this.amount = amount;
        this.duration = duration;
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

    public PushUpEntity toEntity() {
        return new PushUpEntity(id, username, workoutName, amount, duration);
    }
}
