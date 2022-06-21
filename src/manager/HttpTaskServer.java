package manager;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private HttpServer server;
    private TaskManager manager;
    private Gson gson;

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        System.out.println("Закрываем сервер на порту " + PORT);
        server.stop(0);
    }

    HttpTaskServer() throws IOException {
        server = HttpServer.create();
        manager = Managers.getDefault();
        gson = Managers.getGson();

        server.bind(new InetSocketAddress("localhost", PORT), 0);

        server.createContext("/tasks", this::tasksHandler);
        server.createContext("/task/task", this::taskHandler);
        server.createContext("/task/subtask", this::subtaskHandler);
        server.createContext("/task/epic", this::epicHandler);
        server.createContext("/tasks/history", this::historyHandler);

        System.out.println("Сервер запущен");
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer s = new HttpTaskServer();
        s.start();
    }

    private void tasksHandler(HttpExchange httpExchange) throws IOException {
        String response = "";
        List tasks;
        String subMethod = httpExchange.getRequestURI().getPath().substring("/tasks/".length());
        switch (httpExchange.getRequestMethod()) {
            case ("GET"):
                if ("task".equals(subMethod)) {
                    tasks = manager.getAllTasks();
                } else if ("subtask".equals(subMethod)) {
                    tasks = manager.getAllSubTasks();
                } else if ("epic".equals(subMethod)) {
                    tasks = manager.getAllEpic();
                } else {
                    response = "Не распознан подзапрос " + subMethod + " может быть task, subtask, epic!";
                    System.out.println(response);
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
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
                break;
            case ("DELETE"):
                if ("task".equals(subMethod)) {
                    tasks = manager.getAllTasks();
                } else if ("subtask".equals(subMethod)) {
                    tasks = manager.getAllSubTasks();
                } else if ("epic".equals(subMethod)) {
                    tasks = manager.getAllEpic();
                } else {
                    response = "Не распознан подзапрос " + subMethod + " может быть task, subtask, epic!";
                    System.out.println(response);
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (tasks == null || tasks.size() == 0) {
                    response = "Объекты " + subMethod + " отсутствуют!";
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if ("task".equals(subMethod)) {
                    manager.deleteAllTasks();
                } else if ("subtask".equals(subMethod)) {
                    manager.deleteAllSubTasks();
                } else if ("epic".equals(subMethod)) {
                    manager.deleteAllEpic();
                } else {
                    response = "Не распознан подзапрос " + subMethod + " может быть task, subtask, epic!";
                    System.out.println(response);
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                response = "Объекты успешно удалёны!";
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                break;
            default:
                System.out.println("Метод " + httpExchange.getRequestMethod() + " не поддерживается!");
                break;
        }
    }

    private void taskHandler(HttpExchange httpExchange) throws IOException {
        String response = "";
        String query = "";
        int id;
        Task task;
        switch (httpExchange.getRequestMethod()) {
            case ("GET"):
                query = httpExchange.getRequestURI().getQuery();
                if (!checkBody(httpExchange, query)) {
                    return;
                }
                id = Integer.parseInt(
                        queryToMap(query).get("id")
                );
                task = manager.getTask(id);
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
                break;
            case ("POST"):
                String subMethod = httpExchange.getRequestURI().getPath().substring("/task/task/".length());
                String json = getBodyAsString(httpExchange.getRequestBody());
                if (!checkBody(httpExchange, json)) {
                    return;
                }
                if ("create".equals(subMethod)) {
                    manager.createTask(taskFromJsonString(json));
                    response = "Объект успешно создан!";
                } else if ("update".equals(subMethod)) {
                    manager.updateTask(taskFromJsonString(json));
                    response = "Объект успешно обновлён!";
                } else {
                    response = "Не распознан подзапрос " + subMethod + " может быть либо create, либо update!";
                    System.out.println(response);
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                break;
            case ("DELETE"):
                query = getBodyAsString(httpExchange.getRequestBody());
                if (!checkBody(httpExchange, query)) {
                    return;
                }
                id = Integer.parseInt(
                        queryToMap(query).get("id")
                );
                task = manager.getTask(id);
                if (task == null) {
                    response = "Объект с данным id отсутствует!";
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                manager.removeTask(id);
                response = "Объект успешно удалён!";
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                break;
            default:
                System.out.println("Метод " + httpExchange.getRequestMethod() + " не поддерживается!");
                break;
        }
    }

    private void subtaskHandler(HttpExchange httpExchange) throws IOException {
        String response = "";
        String query = "";
        int id;
        SubTask task;
        switch (httpExchange.getRequestMethod()) {
            case ("GET"):
                query = httpExchange.getRequestURI().getQuery();
                if (!checkBody(httpExchange, query)) {
                    return;
                }
                id = Integer.parseInt(
                        queryToMap(query).get("id")
                );
                task = manager.getSubTask(id);
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
                break;
            case ("POST"):
                String subMethod = httpExchange.getRequestURI().getPath().substring("/task/subtask/".length());
                String json = getBodyAsString(httpExchange.getRequestBody());
                if (!checkBody(httpExchange, json)) {
                    return;
                }
                if ("create".equals(subMethod)) {
                    manager.createSubTask(subTaskFromJsonString(json));
                    response = "Объект успешно создан!";
                } else if ("update".equals(subMethod)) {
                    manager.updateSubTask(subTaskFromJsonString(json));
                    response = "Объект успешно обновлён!";
                } else {
                    response = "Не распознан подзапрос " + subMethod + " может быть либо create, либо update!";
                    System.out.println(response);
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                break;
            case ("DELETE"):
                query = getBodyAsString(httpExchange.getRequestBody());
                if (!checkBody(httpExchange, query)) {
                    return;
                }
                id = Integer.parseInt(
                        queryToMap(query).get("id")
                );
                task = manager.getSubTask(id);
                if (task == null) {
                    response = "Объект с данным id отсутствует!";
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                manager.removeSubTask(id);
                response = "Объект успешно удалён!";
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                break;
            default:
                System.out.println("Метод " + httpExchange.getRequestMethod() + " не поддерживается!");
                break;
        }
    }

    private void epicHandler(HttpExchange httpExchange) throws IOException {
        String response = "";
        String query = "";
        int id;
        Epic task;
        switch (httpExchange.getRequestMethod()) {
            case ("GET"):
                query = httpExchange.getRequestURI().getQuery();
                if (!checkBody(httpExchange, query)) {
                    return;
                }
                id = Integer.parseInt(
                        queryToMap(query).get("id")
                );
                task = manager.getEpic(id);
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
                break;
            case ("POST"):
                String subMethod = httpExchange.getRequestURI().getPath().substring("/task/epic/".length());
                String json = getBodyAsString(httpExchange.getRequestBody());
                if (!checkBody(httpExchange, json)) {
                    return;
                }
                if ("create".equals(subMethod)) {
                    manager.createEpic(epicFromJsonString(json));
                    response = "Объект успешно создан!";
                } else if ("update".equals(subMethod)) {
                    manager.updateEpic(epicFromJsonString(json));
                    response = "Объект успешно обновлён!";
                } else {
                    response = "Не распознан подзапрос " + subMethod + " может быть либо create, либо update!";
                    System.out.println(response);
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                break;
            case ("DELETE"):
                query = getBodyAsString(httpExchange.getRequestBody());
                if (!checkBody(httpExchange, query)) {
                    return;
                }
                id = Integer.parseInt(
                        queryToMap(query).get("id")
                );
                task = manager.getEpic(id);
                if (task == null) {
                    response = "Объект с данным id отсутствует!";
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                manager.removeEpic(id);
                response = "Объект успешно удалён!";
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                break;
            default:
                response = "Метод " + httpExchange.getRequestMethod() + " не поддерживается!";
                System.out.println(response);
                break;
        }
    }

    private void historyHandler(HttpExchange httpExchange) throws IOException {
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
        response = gson.toJson(tasks);
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String getBodyAsString(InputStream body) {
        StringBuilder stringBuilder = new StringBuilder(512);
        try {
            InputStreamReader isr = new InputStreamReader(body, "utf-8");
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
        if (query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    private boolean checkMethod(HttpExchange httpExchange, String method) throws IOException {
        if (method.equals(httpExchange.getRequestMethod())) {
            return true;
        } else {
            String response = httpExchange.getRequestURI().getPath() +
                    " ждёт " + method + "-запрос, а получил: " + httpExchange.getRequestMethod();
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
