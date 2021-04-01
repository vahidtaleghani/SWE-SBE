package at.technikumwien.swe.datalayer.models;

public enum TournamentState {
    IN_PROGRESS(0),
    DRAW(1),
    LOSE(2),
    WIN(3),
    FINISHED_BUT_IGNORED(-1);

    private final int value;

    TournamentState(int value) {
        this.value = value;
    }

    public static TournamentState fromValue(int value) {
        for (TournamentState tournamentState : TournamentState.values()) {
            if (tournamentState.getValue() == value) return tournamentState;
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
