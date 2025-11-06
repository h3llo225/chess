package serverFacade;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.DataAccessException;
import model.UserData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
    public HttpRequest requestBuilder(String method, String endpoint, Object body){
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080"+endpoint))
                .method(method, bodyRequest(body));
                request.setHeader("Content-Type", "application/json");

        return request.build();
    }

    public UserData registerUser(UserData user) throws IOException, InterruptedException, DataAccessException {
        HttpRequest request = requestBuilder("POST", "/register", user);
        HttpResponse<String> response = requestSend(request);
        return handleStatusCodeAndResponse(response, UserData.class);
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
