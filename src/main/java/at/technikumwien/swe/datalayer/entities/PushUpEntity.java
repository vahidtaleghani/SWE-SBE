package at.technikumwien.swe.datalayer.entities;

import at.technikumwien.swe.datalayer.models.PushUpModel;

public class PushUpEntity {
    private final int id;
    private final String username, workoutName;
    private final int amount, duration;

    public PushUpEntity(int id, String username, String workoutName, int amount, int duration) {
        this.id = id;
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

    public PushUpModel toModel() {
        return new PushUpModel(id, username, workoutName, amount, duration);
    }
}
