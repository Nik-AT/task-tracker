package manager;

import com.google.gson.Gson;
import model.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KVServerTest {

    private Gson gson = Managers.getGson();

    private static KVServer kvServer;

    @BeforeAll
    static void startServers() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    void addAndGet() {
        try {
            KVClient kvClient = new KVClient("http://localhost:8078");
            kvClient.put("1", "{\n" +
                    "  \"name\": \"Задача1\",\n" +
                    "  \"description\": \"Описание1\",\n" +
                    "  \"status\": \"new\",\n" +
                    "  \"id\": 1,\n" +
                    "  \"startTime\": \"2022-06-07T12:00:00\",\n" +
                    "  \"duration\" : \"215\"\n" +
                    "}");
            kvClient.put("2", "{\n" +
                    "  \"name\": \"Задача2\",\n" +
                    "  \"description\": \"Описание2\",\n" +
                    "  \"status\": \"new\",\n" +
                    "  \"id\": 2,\n" +
                    "  \"startTime\": \"2022-06-08T12:00:00\",\n" +
                    "  \"duration\" : \"215\"\n" +
                    "}");
            kvClient.put("3", "{\n" +
                    "  \"name\": \"Задача3\",\n" +
                    "  \"description\": \"Описание3\",\n" +
                    "  \"status\": \"new\",\n" +
                    "  \"id\": 3,\n" +
                    "  \"startTime\": \"2022-06-09T12:00:00\",\n" +
                    "  \"duration\" : \"215\"\n" +
                    "}");
            Task task = gson.fromJson(kvClient.load("1"), Task.class);
            assertEquals(1, task.getId());
            assertEquals("Задача1", task.getName());
            assertEquals("Описание1", task.getDescription());
            task = gson.fromJson(kvClient.load("2"), Task.class);
            assertEquals(2, task.getId());
            assertEquals("Задача2", task.getName());
            assertEquals("Описание2", task.getDescription());
            task = gson.fromJson(kvClient.load("3"), Task.class);
            assertEquals(3, task.getId());
            assertEquals("Задача3", task.getName());
            assertEquals("Описание3", task.getDescription());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    void updateAndGet() {
        addAndGet();
        try {
            KVClient kvClient = new KVClient("http://localhost:8078");
            kvClient.put("1", "{\n" +
                    "  \"name\": \"Задача1\",\n" +
                    "  \"description\": \"Описание1upd\",\n" +
                    "  \"status\": \"new\",\n" +
                    "  \"id\": 1,\n" +
                    "  \"startTime\": \"2022-06-07T12:00:00\",\n" +
                    "  \"duration\" : \"215\"\n" +
                    "}");
            kvClient.put("2", "{\n" +
                    "  \"name\": \"Задача2\",\n" +
                    "  \"description\": \"Описание2upd\",\n" +
                    "  \"status\": \"new\",\n" +
                    "  \"id\": 2,\n" +
                    "  \"startTime\": \"2022-06-08T12:00:00\",\n" +
                    "  \"duration\" : \"215\"\n" +
                    "}");
            kvClient.put("3", "{\n" +
                    "  \"name\": \"Задача3\",\n" +
                    "  \"description\": \"Описание3upd\",\n" +
                    "  \"status\": \"new\",\n" +
                    "  \"id\": 3,\n" +
                    "  \"startTime\": \"2022-06-09T12:00:00\",\n" +
                    "  \"duration\" : \"215\"\n" +
                    "}");
            Task task = gson.fromJson(kvClient.load("1"), Task.class);
            assertEquals(1, task.getId());
            assertEquals("Задача1", task.getName());
            assertEquals("Описание1upd", task.getDescription());
            task = gson.fromJson(kvClient.load("2"), Task.class);
            assertEquals(2, task.getId());
            assertEquals("Задача2", task.getName());
            assertEquals("Описание2upd", task.getDescription());
            task = gson.fromJson(kvClient.load("3"), Task.class);
            assertEquals(3, task.getId());
            assertEquals("Задача3", task.getName());
            assertEquals("Описание3upd", task.getDescription());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    void removeAndGet() {
        addAndGet();
        try {
            KVClient kvClient = new KVClient("http://localhost:8078");
            kvClient.put("1", "");
            kvClient.put("2", "");
            kvClient.put("3", "");
            Task task = gson.fromJson(kvClient.load("3"), Task.class);
            assertEquals(null, task);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @AfterAll
    static void stopServers() {
        kvServer.stop();
    }
}
