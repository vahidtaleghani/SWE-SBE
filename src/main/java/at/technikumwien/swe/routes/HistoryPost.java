package at.technikumwien.swe.routes;

import at.technikumwien.swe.HttpMethod;
import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;
import at.technikumwien.swe.datalayer.models.PushUpModel;
import at.technikumwien.swe.datalayer.models.UserModel;
import at.technikumwien.swe.datalayer.repositories.PushUpRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HistoryPost extends BasicRoute {


    @Override
    public boolean isResponsibleFor(Request request) {
        return request.getMethod() == HttpMethod.POST && request.getPath().equals("/history");
    }

    @Override
    public Response processRequest(Request request) {

        UserModel loggedUser = validateToken(request.getHeaders().get("authorization"));
        if (loggedUser == null) return Response.Default.unauthorized("No valid authorization token provided.");

        TransferHistoryEntry transferHistoryEntry;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            transferHistoryEntry = objectMapper.readValue(request.getPayload(), TransferHistoryEntry.class);
        } catch (JsonProcessingException e) {
            return Response.Default.invalidJsonProvided();
        }

        if (transferHistoryEntry.amount <= 0 || transferHistoryEntry.duration <= 0 || transferHistoryEntry.workoutName == null) {
            return Response.Default.badRequest("Amount, duration and name must be set correctly");
        }

        PushUpRepository repository = new PushUpRepository();

        if (!repository.addPushUp(transferHistoryEntry.toModel(loggedUser))) {
            return Response.Default.internalServerError();
        }

        return Response.Default.ok();
    }

    private static class TransferHistoryEntry {
        @JsonProperty("Name")
        private String workoutName;

        @JsonProperty("Count")
        private int amount;

        @JsonProperty("DurationInSeconds")
        private int duration;

        PushUpModel toModel(UserModel userModel) {
            return new PushUpModel(userModel.getUsername(), workoutName, amount, duration);
        }
    }
}
