package manager;

import com.google.gson.Gson;
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
    }

    @Override
    public Task getTask(int id) {
        String taskJson = kvClient.load(String.valueOf(id));
        Task task = gson.fromJson(taskJson, Task.class);
        return task;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        kvClient.put(String.valueOf(task.getId()), gson.toJson(task));
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        kvClient.put(String.valueOf(task.getId()), gson.toJson(task));
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        kvClient.put(String.valueOf(id), "");
    }

    @Override
    public SubTask getSubTask(int id) {
        String taskJson = kvClient.load(String.valueOf(id));
        SubTask task = gson.fromJson(taskJson, SubTask.class);
        return task;
    }

    @Override
    public void createSubTask(SubTask task) {
        super.createSubTask(task);
        kvClient.put(String.valueOf(task.getId()), gson.toJson(task));
    }

    @Override
    public void updateSubTask(SubTask task) {
        super.updateSubTask(task);
        kvClient.put(String.valueOf(task.getId()), gson.toJson(task));
    }

    @Override
    public void removeSubTask(int id) {
        super.removeSubTask(id);
        kvClient.put(String.valueOf(id), "");
    }

    @Override
    public Epic getEpic(int id) {
        String taskJson = kvClient.load(String.valueOf(id));
        Epic task = gson.fromJson(taskJson, Epic.class);
        return task;
    }

    @Override
    public void createEpic(Epic task) {
        super.createEpic(task);
        task.setSubTaskArray(new ArrayList<>());
        kvClient.put(String.valueOf(task.getId()), gson.toJson(task));
    }

    @Override
    public void updateEpic(Epic task) {
        super.updateEpic(task);
        kvClient.put(String.valueOf(task.getId()), gson.toJson(task));
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        kvClient.put(String.valueOf(id), "");
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        kvClient.put("", "tasks");
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return super.getAllSubTasks();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        kvClient.put("", "subtasks");
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return super.getAllEpic();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        kvClient.put("", "epics");
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    protected void load() {
        //С файлами не работаем в этой версии менеджера
    }

    @Override
    protected void save() {
        //С файлами не работаем в этой версии менеджера
    }
}
