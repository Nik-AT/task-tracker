package manager;

import com.google.gson.Gson;
import model.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {

    private Gson gson = Managers.getGson();

    static private KVServer kvServer;
    static private HttpTaskServer httpTaskServer;

    @BeforeAll
    static void startServers() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            httpTaskServer = new HttpTaskServer();
            httpTaskServer.server.start();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    void addFromApi() {
        try {
            HttpClientObj httpClientObj = new HttpClientObj();
            String response = httpClientObj.post("http://localhost:8080/task/create", "{\n" +
                    "  \"name\": \"Задача1\",\n" +
                    "  \"description\": \"Описание1upd\",\n" +
                    "  \"status\": \"new\",\n" +
                    "  \"id\": 1,\n" +
                    "  \"startTime\": \"2022-06-07T12:00:00\",\n" +
                    "  \"duration\" : \"215\"\n" +
                    "}");
            assertEquals("Объект успешно создан!", response);
            response = httpClientObj.post("http://localhost:8080/epic/create", "{\n" +
                    "  \"name\": \"Задача1\",\n" +
                    "  \"description\": \"Описание1upd\",\n" +
                    "  \"status\": \"new\",\n" +
                    "  \"id\": 2,\n" +
                    "  \"startTime\": \"2022-06-09T12:00:00\",\n" +
                    "  \"duration\" : \"215\"\n" +
                    "}");
            assertEquals("Объект успешно создан!", response);
            response = httpClientObj.post("http://localhost:8080/subtask/create", "{\n" +
                    "  \"name\": \"Задача1\",\n" +
                    "  \"description\": \"Описание1upd\",\n" +
                    "  \"status\": \"new\",\n" +
                    "  \"id\": 3,\n" +
                    "  \"epicId\": 2,\n" +
                    "  \"startTime\": \"2022-06-08T12:00:00\",\n" +
                    "  \"duration\" : \"215\"\n" +
                    "}");
            assertEquals("Объект успешно создан!", response);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @AfterAll
    static void stopServers() {
        kvServer.stop();
        httpTaskServer.server.stop(0);
    }
}
