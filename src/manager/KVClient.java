package manager;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVClient {

    private String urlServer;

    String token = "DEBUG"; // KVServer register

    public KVClient(String url) throws IOException {
        urlServer = url;
        register();
    }


    public void put(String key, String value) {
        String postEndpoint = urlServer + "/save/" + key + "?API_TOKEN=" + token;
        if ("tasks".equals(value) || "subtasks".equals(value) || "epics".equals(value)) {
            postEndpoint = urlServer + "/save/" + value + "?API_TOKEN=" + token;
        }
        try {
            HttpRequest request;
            if (key.isEmpty()) {
                request = HttpRequest.newBuilder()
                        .uri(URI.create(postEndpoint))
                        .header("Content-Type", "application/json")
                        .DELETE()
                        .build();
            } else if (value.isEmpty()) {
                request = HttpRequest.newBuilder()
                        .uri(URI.create(postEndpoint))
                        .header("Content-Type", "application/json")
                        .DELETE()
                        .build();
            } else {
                request = HttpRequest.newBuilder()
                        .uri(URI.create(postEndpoint))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(value))
                        .build();
            }

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            System.out.println(response.body());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
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
            System.out.println(response.statusCode());
            System.out.println(response.body());
            return response.body().toString();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
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
            System.out.println(response.statusCode());
            System.out.println(" body " + response.body());
            token = response.body().toString();
            return response.body().toString();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
