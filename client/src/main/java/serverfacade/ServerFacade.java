package serverfacade;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.TransitoryGameData;
import model.UserData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;


public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    public int port;

    public ServerFacade(int port){
        this.port = port;
    }

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
                .uri(URI.create("http://localhost:"+port+endpoint));
        if (authToken != null) {
            request.header("authorization", authToken);
        }
                request.method(method, bodyRequest(body));
                request.setHeader("Content-Type", "application/json");
        return request.build();
    }



    public AuthData registerUser(UserData user) throws Exception {
        HttpRequest request = requestBuilder("POST", "/user", user, "");
        HttpResponse<String> response = requestSend(request);
        return handleStatusCodeAndResponse(response, AuthData.class);
    }
    public AuthData loginUser(UserData user) throws Exception {
        HttpRequest request = requestBuilder("POST", "/session", user, "");
        HttpResponse<String> response = requestSend(request);
        return handleStatusCodeAndResponse(response, AuthData.class);
    }
    public String logoutUser(String authToken) throws Exception {
        HttpRequest request = requestBuilder("DELETE", "/session","", authToken);
        HttpResponse<String> response = requestSend(request);
        return handleStatusCodeAndResponse(response, String.class);
    }

    public String createGame(GameData gameStuff, String authToken) throws Exception {
        HttpRequest request = requestBuilder("POST", "/game", gameStuff, authToken);
        HttpResponse<String> response = requestSend(request);
        return handleStatusCodeAndResponseCreateGame(response, String.class);
    }

    public String listGame(String authToken) throws Exception {
        HttpRequest request = requestBuilder("GET", "/game", "", authToken);
        HttpResponse<String> response = requestSend(request);
        return handleStatusCodeAndResponseCreateGame(response, String.class);
    }
    public String playGame(TransitoryGameData joinGameData, String authToken) throws Exception {
        HttpRequest request = requestBuilder("PUT", "/game", joinGameData, authToken);
        HttpResponse<String> response = requestSend(request);
        return handleStatusCodeAndResponse(response, String.class);
    }


    public String clearDB() throws Exception {
        HttpRequest request = requestBuilder("DELETE", "/db", "", "");
        HttpResponse<String> response = requestSend(request);
        return handleStatusCodeAndResponse(response, String.class);
    }



    public <T> T handleStatusCodeAndResponse(HttpResponse<String> response, Class<T> customClass) throws Exception {
        try {int status = response.statusCode();
        String body = response.body();
        if (status < 199 || status > 299){
            throw new Exception(body);
        }
        if (Objects.equals(body, "{}")){
            return (T) "{}";
        }
        return new Gson().fromJson(body, customClass);
    }catch(Exception ex){
        throw new Exception(ex.getMessage());    }

}

    public <T> T handleStatusCodeAndResponseCreateGame(HttpResponse<String> response, Class<T> customClass) throws Exception {
        try {int status = response.statusCode();
            String body = response.body();
            if (status < 199 || status > 299){
                throw new Exception(body);
            }
            if (Objects.equals(body, "{}")){
                return (T) "{}";
            }
            return (T) body;
        }catch(Exception ex){
            throw new Exception(ex.getMessage());    }

    }
}
