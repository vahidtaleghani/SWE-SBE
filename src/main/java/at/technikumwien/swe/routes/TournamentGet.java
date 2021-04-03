package at.technikumwien.swe.routes;

import at.technikumwien.swe.HttpMethod;
import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;
import at.technikumwien.swe.TournamentController;
import at.technikumwien.swe.datalayer.models.PushUpModel;
import at.technikumwien.swe.datalayer.models.UserModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class TournamentGet extends BasicRoute {
    public boolean isResponsibleFor(Request request) {
        return request.getMethod() == HttpMethod.GET && request.getPath().equals("/tournament");
    }

    @Override
    public Response processRequest(Request request) {

        UserModel loggedUser = validateToken(request.getHeaders().get("authorization"));
        if (loggedUser == null) return Response.Default.unauthorized("No valid authorization token provided.");

        List<PushUpModel> openPushUps = TournamentController.getOpenPushUps();
        if (openPushUps == null) {
            return Response.Default.json(new NoTournamentTransferObject());
        }

        // für alle Teilnehmer*in dass in dieser Liste liegt, gibt die summe von amount zurück
        // und speichern in ein HashMap
        Map<String, Integer> pushUpSumMap = TournamentController.calculateSumsPerUser(openPushUps);

        List<String> winnerUserNameList = TournamentController.findWinningUserNames(pushUpSumMap);

        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String strTime = timeFormat.format(openPushUps.get(0).getAddedTime());

        // rechnen die Anzahl der Aktiven Teilnehmer*in
        int countUsername = pushUpSumMap.size();

        return Response.Default.json(new TournamentTransferObject(winnerUserNameList, countUsername, strTime));
    }

    private static class NoTournamentTransferObject {
        public final boolean activeTournament = false;
    }

    private static class TournamentTransferObject {
        public final boolean activeTournament = true;
        public final List<String> inFront;
        public final int participates;
        public final String startTime;

        public TournamentTransferObject(List<String> inFront, int participates, String startTime) {
            this.inFront = inFront;
            this.participates = participates;
            this.startTime = startTime;
        }
    }
}
