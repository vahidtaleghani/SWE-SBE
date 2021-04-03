package at.technikumwien.swe;

import at.technikumwien.swe.datalayer.models.PushUpModel;
import at.technikumwien.swe.datalayer.models.TournamentState;
import at.technikumwien.swe.datalayer.repositories.PushUpRepository;

import java.util.*;

public class TournamentController {

    public static void handle() {
        List<PushUpModel> openPushUps = getOpenPushUps();
        if (openPushUps == null) return;

        handleAllTournaments(openPushUps);
    }

    // Gibt alle Ziele von push-ups Tabelle,
    // dass tournament_state = 0 in ein geordneten Liste zurück
    public static List<PushUpModel> getOpenPushUps() {
        PushUpRepository pushUpRepository = new PushUpRepository();

        List<PushUpModel> allModelsWithoutTournament = pushUpRepository.getAll(TournamentState.IN_PROGRESS);

        if (allModelsWithoutTournament.isEmpty()) return null;

        allModelsWithoutTournament.sort((e1, e2) -> (int) (e1.getAddedTime().getTime() - e2.getAddedTime().getTime()));

        return allModelsWithoutTournament;
    }

    // Entfernen Sie Zeilen weniger als zwei Minuten nach der Registrierung aus der Liste
    // Wählen Sie die Startzeit des letzten Teilnehmers aus und erstellen Sie eine neue Liste,
    // deren Startzeit nach der Startzeit des letzten Teilnehmers
    // und vor dem Ende der letzten zwei Minuten der Teilnahme liegt
    // Fahren Sie fort, bis die ursprüngliche Liste leer ist
    private static void handleAllTournaments(List<PushUpModel> allModelsWithoutTournament) {

        int twoMinutesIntervalMilliseconds = 2 * 60 * 1000;
        Date twoMinutesAgo = new Date(new Date().getTime() - twoMinutesIntervalMilliseconds);

        while (!allModelsWithoutTournament.isEmpty() && allModelsWithoutTournament.get(0).getAddedTime().before(twoMinutesAgo)) {

            Date tournamentStart = allModelsWithoutTournament.get(0).getAddedTime();
            Date tournamentEnd = new Date(tournamentStart.getTime() + twoMinutesIntervalMilliseconds);

            List<PushUpModel> currentTournamentList = new LinkedList<>();

            System.out.println(allModelsWithoutTournament);

            for (PushUpModel model : allModelsWithoutTournament) {
                if (model.getAddedTime().equals(tournamentStart) ||
                        model.getAddedTime().after(tournamentStart) && model.getAddedTime().before(tournamentEnd)
                ) {
                    currentTournamentList.add(model);
                }
            }
            allModelsWithoutTournament.removeAll(currentTournamentList);

            handleSingleTournament(currentTournamentList);
        }
    }

    public static Map<String, Integer> calculateSumsPerUser(List<PushUpModel> currentTournamentList) {

        Map<String, Integer> pushUpSumMap = new HashMap<>();

        // für alle Teilnehmer*in dass in dieser Liste liegt, gibt die summe von amount zurück
        // und speichern in ein HashMap
        for (PushUpModel model : currentTournamentList) {
            Integer amount = model.getAmount();
            if (pushUpSumMap.containsKey(model.getUsername())) {
                amount += pushUpSumMap.get(model.getUsername());
            }
            pushUpSumMap.put(model.getUsername(), amount);
        }

        return pushUpSumMap;
    }

    public static List<String> findWinningUserNames(Map<String, Integer> pushUpSumMap) {

        int maxPushUps = -1;
        List<String> winnerUserNameList = new LinkedList<>();

        // finden die Gewinner*in/en und speichern in einer Liste
        for (Map.Entry<String, Integer> entry : pushUpSumMap.entrySet()) {
            if (entry.getValue() >= maxPushUps) {
                if (entry.getValue() > maxPushUps) {
                    winnerUserNameList.clear();
                    maxPushUps = entry.getValue();
                }
                winnerUserNameList.add(entry.getKey());
            }
        }
        return winnerUserNameList;
    }

    // Berechnen die summe(amount) von alle Teilnehmer*in
    // Berechnet Gewinn, Verlust oder Gleichstand
    // und ändern tournament_state Spalte in push-ups Tabelle
    private static void handleSingleTournament(List<PushUpModel> currentTournamentList) {

        System.out.println("Single Tournament: " + currentTournamentList);

        Map<String, Integer> pushUpSumMap = calculateSumsPerUser(currentTournamentList);

        List<String> winnerUserNameList = findWinningUserNames(pushUpSumMap);

        boolean isDraw = winnerUserNameList.size() > 1;

        List<String> pointsEnteredUserNamesList = new LinkedList<>();

        for (PushUpModel model : currentTournamentList) {

            //Steuert, dass ein Teilnehmer*in in einer Liste nicht zweimal gewinnt
            if (pointsEnteredUserNamesList.contains(model.getUsername())) {
                model.setTournamentState(TournamentState.FINISHED_BUT_IGNORED);
                continue;
            }

            pointsEnteredUserNamesList.add(model.getUsername());

            // wenn die Name von Teilnehmer*in in der List liegt -> draw oder win
            if (winnerUserNameList.contains(model.getUsername())) {

                // Wenn die Liste mehr als einen Teilnehmer*in enthält, bedeutet dies Gleichheit
                if (isDraw) model.setTournamentState(TournamentState.DRAW);

                else model.setTournamentState(TournamentState.WIN);
            }
            // wenn die Name von Teilnehmer*in in der List nicht liegt -> lose
            else {
                model.setTournamentState(TournamentState.LOSE);
            }
        }

        // DB ändern
        PushUpRepository pushUpRepository = new PushUpRepository();
        for (PushUpModel model : currentTournamentList) {
            pushUpRepository.update(model);
        }
    }
}
