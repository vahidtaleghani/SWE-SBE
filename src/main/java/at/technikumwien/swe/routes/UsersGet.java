package at.technikumwien.swe.routes;

import at.technikumwien.swe.HttpMethod;
import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;
import at.technikumwien.swe.datalayer.models.UserModel;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UsersGet extends BasicRoute {

    private static final String routePrefix = "/users/";

    @Override
    public boolean isResponsibleFor(Request request) {
        return
                request.getMethod() == HttpMethod.GET &&
                        request.getPath().startsWith(routePrefix) &&
                        request.getPath().length() > routePrefix.length();
    }

    @Override
    public Response processRequest(Request request) {

        String requestedUserName = request.getPath().substring(routePrefix.length());

        // speichern alle informationen zu angemeldeten Benutzern
        UserModel loggedUser = validateToken(request.getHeaders().get("authorization"));
        if (loggedUser == null) return Response.Default.unauthorized("No valid authorization token provided.");

        if (!loggedUser.getUsername().equals(requestedUserName)) {
            return Response.Default.unauthorized("You are not authorized to view this profile");
        }

        return Response.Default.json(TransferUser.fromModel(loggedUser));
    }


    @SuppressWarnings("FieldCanBeLocal")
    private static class TransferUser {
        @JsonProperty("Username")
        private final String username;

        @JsonProperty("Name")
        private final String name;

        @JsonProperty("Bio")
        private final String biography;

        @JsonProperty("Image")
        private final String image;

        public TransferUser(String username, String name, String biography, String image) {
            this.username = username;
            this.name = name;
            this.biography = biography;
            this.image = image;
        }

        static TransferUser fromModel(UserModel userModel) {
            return new TransferUser(
                    userModel.getUsername(),
                    userModel.getName(),
                    userModel.getBiography(),
                    userModel.getImage()
            );
        }
    }
}
