package manager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVClient {

    private String urlServer;

    private String token = "DEBUG"; // KVServer register

    public KVClient(String url) throws IOException {
        urlServer = url;
        register();
    }

    public String load(String key) {
        String postEndpoint = urlServer + "/load/" + key + "?API_TOKEN=" + token;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(postEndpoint))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status code " + response.statusCode());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Не выполнен запрос GET к KVServer status code " + response.statusCode());
            }
            System.out.println("Response body " + response.body());
            return response.body().toString();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public void put(String key, String value) {
        String postEndpoint = urlServer + "/save/" + key + "?API_TOKEN=" + token;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(postEndpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status code " + response.statusCode());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Не выполнен запрос POST к KVServer status code " + response.statusCode());
            }
            System.out.println("Response body " + response.body());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String register() {
        String postEndpoint = urlServer + "/register";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(postEndpoint))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status code " + response.statusCode());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Не выполнена регистрация на KVServer status code " + response.statusCode());
            }
            System.out.println("Response body " + response.body());
            token = response.body().toString();
            return response.body().toString();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
