package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVClient kvClient;
    private Gson gson = Managers.getGson();

    public HttpTaskManager(String kvServerUrl) throws IOException {
        super(null);
        kvClient = new KVClient(kvServerUrl);
        load();
    }

//    @Override
//    public List<Task> getHistory() {
//        return super.getHistory();
//    }

    @Override
    protected void load() {
        ArrayList<Task> tasks = gson.fromJson(kvClient.load("tasks"),
                new TypeToken<ArrayList<Task>>() {
                }.getType());
        ArrayList<SubTask> subtasks = gson.fromJson(kvClient.load("subtasks"),
                new TypeToken<ArrayList<SubTask>>() {
                }.getType());
        ArrayList<Epic> epics = gson.fromJson(kvClient.load("epics"),
                new TypeToken<ArrayList<Epic>>() {
                }.getType());
        ArrayList<Integer> history = gson.fromJson(kvClient.load("history"),
                new TypeToken<ArrayList<Integer>>() {
                }.getType());
        if (tasks != null) {
            for (int i = 0; i < tasks.size(); i++) {
                taskHashMap.put(tasks.get(i).getId(), tasks.get(i));
            }
        }
        if (subtasks != null) {
            for (int i = 0; i < subtasks.size(); i++) {
                subTaskHashMap.put(subtasks.get(i).getId(), subtasks.get(i));
            }
        }
        if (epics != null) {
            for (int i = 0; i < epics.size(); i++) {
                taskHashMap.put(epics.get(i).getId(), epics.get(i));
            }
        }
        if (history != null) {
            addToHistoryManager(history);
        }
    }

    @Override
    protected void save() {
        String jsonTasks = gson.toJson(new ArrayList<>(taskHashMap.values()));
        kvClient.put("tasks", jsonTasks);
        jsonTasks = gson.toJson(new ArrayList<>(subTaskHashMap.values()));
        kvClient.put("subtasks", jsonTasks);
        jsonTasks = gson.toJson(new ArrayList<>(epicHashMap.values()));
        kvClient.put("epics", jsonTasks);
        jsonTasks = gson.toJson(new ArrayList<>(historyManager.getHistory()));
        kvClient.put("history", jsonTasks);
    }
}
