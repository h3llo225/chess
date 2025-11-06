package serverFacade;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.TransitoryGameData;
import model.UserData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.header;

public class serverFacade {
    private final HttpClient client = HttpClient.newHttpClient();

    public HttpResponse<String> requestSend(HttpRequest request) throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    public HttpRequest.BodyPublisher bodyRequest(Object body){
        if (body != null){
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(body));
        }
        return null;
    }
    public HttpRequest requestBuilder(String method, String endpoint, Object body, String authToken){
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080"+endpoint))
                .header("authorization", authToken)
                .method(method, bodyRequest(body));
                request.setHeader("Content-Type", "application/json");


        return request.build();
    }



    public AuthData registerUser(UserData user) throws IOException, InterruptedException, DataAccessException {
        HttpRequest request = requestBuilder("POST", "/register", user, null);
        HttpResponse<String> response = requestSend(request);
        return handleStatusCodeAndResponse(response, AuthData.class);
    }
    public AuthData loginUser(UserData user) throws IOException, InterruptedException, DataAccessException {
        HttpRequest request = requestBuilder("POST", "/login", user, null);
        HttpResponse<String> response = requestSend(request);
        return handleStatusCodeAndResponse(response, AuthData.class);
    }
    public AuthData logoutUser(String authToken) throws IOException, InterruptedException, DataAccessException {
        HttpRequest request = requestBuilder("DELETE", "/logout",null, authToken);
        HttpResponse<String> response = requestSend(request);
        return handleStatusCodeAndResponse(response, AuthData.class);
    }

    public String createGame(String gameName, String authToken) throws IOException, InterruptedException, DataAccessException {
        HttpRequest request = requestBuilder("POST", "/createGame", gameName, authToken);
        HttpResponse<String> response = requestSend(request);
        return handleStatusCodeAndResponse(response, String.class);
    }

    public String listGame(String authToken) throws IOException, InterruptedException, DataAccessException {
        HttpRequest request = requestBuilder("POST", "/listGame", null, authToken);
        HttpResponse<String> response = requestSend(request);
        return handleStatusCodeAndResponse(response, String.class);
    }
    public String playGame(TransitoryGameData joinGameData, String authToken) throws IOException, InterruptedException, DataAccessException {
        HttpRequest request = requestBuilder("POST", "/listGame", joinGameData, authToken);
        HttpResponse<String> response = requestSend(request);
        return handleStatusCodeAndResponse(response, String.class);
    }



    public <T> T handleStatusCodeAndResponse(HttpResponse<String> response, Class<T> customClass) throws DataAccessException {
        try {int status = response.statusCode();
        String body = response.body();
        if (status < 199 || status > 299){
            throw new DataAccessException(body);
        }
        return new Gson().fromJson(body, customClass);
    }catch(DataAccessException ex){
        throw new DataAccessException(ex.getMessage());    }

}
}
