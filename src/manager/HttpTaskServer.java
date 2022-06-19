package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTaskServer {

    private static final int PORT = 8080;
    HttpServer server;
    TaskManager manager;
    Gson gson;

    HttpTaskServer () throws IOException {
        server = HttpServer.create();
        manager = Managers.getDefault();
        gson = Managers.getGson();

        server.bind(new InetSocketAddress("localhost", PORT), 0);

        server.createContext("/task/get", this::getTaskHandler);
        server.createContext("/task/create", this::createTaskHandler);
        server.createContext("/task/update", this::updateTaskHandler);
        server.createContext("/task/remove", this::removeTaskHandler);

        server.createContext("/tasks/get", this::getAllTasksHandler);
        server.createContext("/tasks/delete", this::deleteAllTasksHandler);

        server.createContext("/subtask/get", this::getTaskHandler);
        server.createContext("/subtask/create", this::createTaskHandler);
        server.createContext("/subtask/update", this::updateTaskHandler);
        server.createContext("/subtask/remove", this::removeTaskHandler);

        server.createContext("/subtasks/get", this::getAllSubTasksHandler);
        server.createContext("/subtasks/delete", this::deleteAllSubTasksHandler);

        server.createContext("/epic/get", this::getTaskHandler);
        server.createContext("/epic/create", this::createTaskHandler);
        server.createContext("/epic/update", this::updateTaskHandler);
        server.createContext("/epic/remove", this::removeTaskHandler);

        server.createContext("/epics/get", this::getAllEpicHandler);
        server.createContext("/epics/delete", this::deleteAllEpicHandler);

        server.createContext("/history", this::getHistoryHandler);

        System.out.println("Сервер запущен");
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer s = new HttpTaskServer();
        s.server.start();
    }

    private void getTaskHandler(HttpExchange httpExchange) throws IOException {
        if (!checkMethod(httpExchange, "GET")) {
            return;
        }
        String query = httpExchange.getRequestURI().getQuery();
        if (!checkBody(httpExchange, query)) {
            return;
        }
        int id = Integer.parseInt(
                queryToMap(query).get("id")
        );
        Task task = getTaskByAPIType(httpExchange.getRequestURI().getPath(), id);
        String response = "";
        if (task == null) {
            response = "Объект с данным id отсутствует!";
            httpExchange.sendResponseHeaders(404, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }
        response = jsonString(task);
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void createTaskHandler(HttpExchange httpExchange) throws IOException {
        if (!checkMethod(httpExchange, "POST")) {
            return;
        }
        String json = getBodyAsString(httpExchange.getRequestBody());
        if (!checkBody(httpExchange, json)) {
            return;
        }
        createTaskByAPIType(httpExchange.getRequestURI().getPath(), json);
        String response = "Объект успешно создан!";
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void updateTaskHandler(HttpExchange httpExchange) throws IOException {
        if (!checkMethod(httpExchange, "POST")) {
            return;
        }
        String json = getBodyAsString(httpExchange.getRequestBody());
        if (!checkBody(httpExchange, json)) {
            return;
        }
        String response = "";
        Task task = getTaskObject(httpExchange.getRequestURI().getPath(), json);
        if (task == null) {
            response = "Объект с данным id отсутствует!";
            httpExchange.sendResponseHeaders(404, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }
        updateTaskByAPIType(httpExchange.getRequestURI().getPath(), json);
        response = "Объект успешно обновлён!";
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void removeTaskHandler(HttpExchange httpExchange) throws IOException {
        if (!checkMethod(httpExchange, "DELETE")) {
            return;
        }
        String query =  getBodyAsString(httpExchange.getRequestBody());
        if (!checkBody(httpExchange, query)) {
            return;
        }
        int id = Integer.parseInt(
                queryToMap(query).get("id")
        );
        String removePath = httpExchange.getRequestURI().getPath();
        String getPath = removePath.substring(0, removePath.indexOf("/remove")) + "/get";
        Task task = getTaskByAPIType(getPath, id);
        String response = "";
        if (task == null) {
            response = "Объект с данным id отсутствует!";
            httpExchange.sendResponseHeaders(404, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }
        removeTaskByAPIType(httpExchange.getRequestURI().getPath(), id);
        response = "Объект успешно удалён!";
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void getAllTasksHandler(HttpExchange httpExchange) throws IOException {
        if (!checkMethod(httpExchange, "GET")) {
            return;
        }

        List<Task> tasks = manager.getAllTasks();
        String response = "";
        if (tasks == null || tasks.size() == 0) {
            response = "Задачи отсутствуют!";
            httpExchange.sendResponseHeaders(404, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }
        String fullJson = "[";
        for (int i = 0; i < tasks.size(); i++) {
            fullJson += jsonString(tasks.get(i));
        }
        fullJson += "]";
        response = jsonString(fullJson);
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void deleteAllTasksHandler(HttpExchange httpExchange) throws IOException {
        if (!checkMethod(httpExchange, "DELETE")) {
            return;
        }
        List<Task> tasks = manager.getAllTasks();
        String response = "";
        if (tasks == null || tasks.size() == 0) {
            response = "Задачи отсутствуют!";
            httpExchange.sendResponseHeaders(404, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }
        manager.deleteAllTasks();
        response = "Объекты успешно удалены!";
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void getAllSubTasksHandler(HttpExchange httpExchange) throws IOException {
        if (!checkMethod(httpExchange, "GET")) {
            return;
        }
        List<SubTask> tasks = manager.getAllSubTasks();
        String response = "";
        if (tasks == null || tasks.size() == 0) {
            response = "Подзадачи отсутствуют!";
            httpExchange.sendResponseHeaders(404, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }
        String fullJson = "[";
        for (int i = 0; i < tasks.size(); i++) {
            fullJson += jsonString(tasks.get(i));
        }
        fullJson += "]";
        response = jsonString(fullJson);
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void deleteAllSubTasksHandler(HttpExchange httpExchange) throws IOException {
        if (!checkMethod(httpExchange, "DELETE")) {
            return;
        }
        List<SubTask> tasks = manager.getAllSubTasks();
        String response = "";
        if (tasks == null || tasks.size() == 0) {
            response = "Подзадачи отсутствуют!";
            httpExchange.sendResponseHeaders(404, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }
        manager.deleteAllSubTasks();
        response = "Объекты успешно удалёны!";
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void getAllEpicHandler(HttpExchange httpExchange) throws IOException {
        if (!checkMethod(httpExchange, "GET")) {
            return;
        }
        List<Epic> tasks = manager.getAllEpic();
        String response = "";
        if (tasks == null || tasks.size() == 0) {
            response = "Эпики отсутствуют!";
            httpExchange.sendResponseHeaders(404, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }
        String fullJson = "[";
        for (int i = 0; i < tasks.size(); i++) {
            fullJson += jsonString(tasks.get(i));
        }
        fullJson += "]";
        response = jsonString(fullJson);
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void deleteAllEpicHandler(HttpExchange httpExchange) throws IOException {
        if (!checkMethod(httpExchange, "DELETE")) {
            return;
        }
        List<Epic> tasks = manager.getAllEpic();
        String response = "";
        if (tasks == null || tasks.size() == 0) {
            response = "Эпики отсутствуют!";
            httpExchange.sendResponseHeaders(404, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }
        manager.deleteAllEpic();
        response = "Объекты успешно удалёны!";
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void getHistoryHandler(HttpExchange httpExchange) throws IOException {
        if (!checkMethod(httpExchange, "GET")) {
            return;
        }
        List<Task> tasks = manager.getHistory();
        String response = "";
        if (tasks == null || tasks.size() == 0) {
            response = "Эпики отсутствуют!";
            httpExchange.sendResponseHeaders(404, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }
        String fullJson = "[";
        for (int i = 0; i < tasks.size(); i++) {
            fullJson += jsonString(tasks.get(i));
        }
        fullJson += "]";
        response = jsonString(fullJson);
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String getBodyAsString(InputStream body) {
        StringBuilder stringBuilder = new StringBuilder(512);
        try {
            InputStreamReader isr = new InputStreamReader(body,"utf-8");
            BufferedReader br = new BufferedReader(isr);
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return stringBuilder.toString();
    }

    private Task taskFromJsonString(String jsonString) {
        return gson.fromJson(jsonString, Task.class);
    }

    private SubTask subTaskFromJsonString(String jsonString) {
        return gson.fromJson(jsonString, SubTask.class);
    }

    private Epic epicFromJsonString(String jsonString) {
        return gson.fromJson(jsonString, Epic.class);
    }

    private String jsonString(Object object) {
        return gson.toJson(object);
    }

    public Map<String, String> queryToMap(String query) {
        if(query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

    public Task getTaskObject(String query, String json) {
        switch (query) {
            case ("/task/get"):
                Task task = taskFromJsonString(json);
                return manager.getTask(task.getId());
            case ("/subtask/get"):
                SubTask subTask = subTaskFromJsonString(json);
                return manager.getSubTask(subTask.getId());
            case ("/epic/get"):
                Epic epic = epicFromJsonString(json);
                return manager.getEpic(epic.getId());
            default:
                return null;
        }
    }

    public Task getTaskByAPIType(String query, int id) {
        switch (query) {
            case ("/task/get"):
                return manager.getTask(id);
            case ("/subtask/get"):
                return manager.getSubTask(id);
            case ("/epic/get"):
                return manager.getEpic(id);
            default:
                return null;
        }
    }

    public void createTaskByAPIType(String query, String jsonString) {
        switch (query) {
            case ("/task/create"):
                manager.createTask(taskFromJsonString(jsonString));
                break;
            case ("/subtask/create"):
                manager.createSubTask(subTaskFromJsonString(jsonString));
                break;
            case ("/epic/create"):
                manager.createEpic(epicFromJsonString(jsonString));
                break;
            default:
                break;
        }
    }

    public void updateTaskByAPIType(String query, String jsonString) {
        switch (query) {
            case ("/task/update"):
                manager.updateTask(taskFromJsonString(jsonString));
                break;
            case ("/subtask/update"):
                manager.updateSubTask(subTaskFromJsonString(jsonString));
                break;
            case ("/epic/update"):
                manager.updateEpic(epicFromJsonString(jsonString));
                break;
            default:
                break;
        }
    }

    public void removeTaskByAPIType(String query, int id) {
        switch (query) {
            case  ("/task/remove"):
                manager.removeTask(id);
                break;
            case ("/subtask/remove"):
                manager.removeSubTask(id);
                break;
            case ("/epic/remove"):
                manager.removeEpic(id);
                break;
            default:
                break;
        }
    }

    private boolean checkMethod(HttpExchange httpExchange, String method) throws IOException {
        if (method.equals(httpExchange.getRequestMethod())) {
            return true;
        } else {
            String response = httpExchange.getRequestURI().getPath() +
                    " ждёт "+ method + "-запрос, а получил: " + httpExchange.getRequestMethod();
            System.out.println(response);
            httpExchange.sendResponseHeaders(405, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return false;
        }
    }

    private boolean checkBody(HttpExchange httpExchange, String body) throws IOException {
        if (!body.isEmpty()) {
            return true;
        } else {
            String response = "Отсутствуют параметры или тело запроса!";
            System.out.println(response);
            httpExchange.sendResponseHeaders(405, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return false;
        }
    }
}
