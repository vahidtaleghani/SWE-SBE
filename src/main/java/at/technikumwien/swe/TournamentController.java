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

    private static List<PushUpModel> getOpenPushUps() {
        PushUpRepository pushUpRepository = new PushUpRepository();

        List<PushUpModel> allModelsWithoutTournament = pushUpRepository.getAll(TournamentState.IN_PROGRESS);

        if (allModelsWithoutTournament.isEmpty()) return null;

        allModelsWithoutTournament.sort((e1, e2) -> (int) (e1.getAddedTime().getTime() - e2.getAddedTime().getTime()));

        return allModelsWithoutTournament;
    }

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

    private static void handleSingleTournament(List<PushUpModel> currentTournamentList) {

        System.out.println("Single Tournament: " + currentTournamentList);

        Map<String, Integer> pushUpSumMap = new HashMap<>();

        for (PushUpModel model : currentTournamentList) {
            Integer amount = model.getAmount();
            if (pushUpSumMap.containsKey(model.getUsername())) {
                amount += pushUpSumMap.get(model.getUsername());
            }
            pushUpSumMap.put(model.getUsername(), amount);
        }

        int maxPushUps = -1;
        List<String> winnerUserNameList = new LinkedList<>();

        for (Map.Entry<String, Integer> entry : pushUpSumMap.entrySet()) {
            if (entry.getValue() >= maxPushUps) {
                if (entry.getValue() > maxPushUps) {
                    winnerUserNameList.clear();
                    maxPushUps = entry.getValue();
                }
                winnerUserNameList.add(entry.getKey());
            }
        }

        boolean isDraw = winnerUserNameList.size() > 1;

        List<String> pointsEnteredUserNamesList = new LinkedList<>();

        for (PushUpModel model : currentTournamentList) {
            if (pointsEnteredUserNamesList.contains(model.getUsername())) {
                model.setTournamentState(TournamentState.FINISHED_BUT_IGNORED);
                continue;
            }
            pointsEnteredUserNamesList.add(model.getUsername());
            if (winnerUserNameList.contains(model.getUsername())) {
                if (isDraw) model.setTournamentState(TournamentState.DRAW);
                else model.setTournamentState(TournamentState.WIN);
            } else {
                model.setTournamentState(TournamentState.LOSE);
            }
        }

        PushUpRepository pushUpRepository = new PushUpRepository();
        for (PushUpModel model : currentTournamentList) {
            pushUpRepository.update(model);
        }
    }
}
