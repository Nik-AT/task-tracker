package manager;

import model.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static model.TypeTask.SUBTASK;
import static model.TypeTask.TASK;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this(file, false);
    }

    public FileBackedTasksManager(File file, boolean load) {
        this.file = file;
//        if (load) {
//            load();
//        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        return task;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return super.getAllSubTasks();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        return subTask;
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void removeSubTask(int id) {
        super.removeSubTask(id);
        save();
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return super.getAllEpic();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();

    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = super.getHistory();
        save();
        return history;
    }

    protected void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append("id,type,name,status,description,start,duration,epic");
            writer.newLine();
            for (Map.Entry<Integer, Task> entry : taskHashMap.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : epicHashMap.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, SubTask> entry : subTaskHashMap.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            writer.append("\n" + toStringHistoryManager(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка!");
        }
    }

    private String toString(Task task) {
        String taskString = task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription() + "," + task.getStartTime() + "," + task.getDuration() + ",";
        if (task.getType().equals(SUBTASK)) {
            SubTask subTask = (SubTask) task;
            taskString = taskString + subTask.getEpicId() + ",";
        }
        return taskString;
    }

    static String toStringHistoryManager(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId()).append(",");
        }
        return sb.toString();
    }

    private static Task fromString(String value) {
        final String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        TypeTask typeTask = TypeTask.valueOf(split[1]);
        String name = split[2];
        Status status = Status.valueOf(split[3]);
        String description = split[4];
        LocalDateTime start = LocalDateTime.parse(split[5]);
        int duration = Integer.parseInt(split[6]);
        switch (typeTask) {
            case TASK:
                return new Task(name, description, status, id, start, duration);
            case SUBTASK:
                return new SubTask(name, description, status, id, start, duration, Integer.parseInt(split[7]));
            case EPIC:
                return new Epic(name, description, status, id, start, duration);
        }
        return null;
    }

    static List<Integer> fromStringInt(String value) {
        final String[] id = value.split(",");
        List<Integer> history = new ArrayList<>();
        for (String v : id) {
            history.add(Integer.valueOf(v));
        }
        return history;
    }

    protected void load() {
        try (final BufferedReader reader = new BufferedReader(new FileReader(file, UTF_8))) {
            reader.readLine();
            String line = reader.readLine();
            while (!line.isEmpty()) {
                final Task task = fromString(line);
                final int id = task.getId();
                if (task.getType() == TASK) {
                    taskHashMap.put(id, task);
                } else if (task.getType() == TypeTask.SUBTASK) {
                    subTaskHashMap.put(id, (SubTask) task);
                    Epic epic = epicHashMap.get(((SubTask) task).getEpicId());
                    epic.getSubTaskArray().add((SubTask) task);
                } else if (task.getType() == TypeTask.EPIC) {
                    epicHashMap.put(id, (Epic) task);
                }
                line = reader.readLine();
            }
            line = reader.readLine();
            List<Integer> list = fromStringInt(line);
            addToHistoryManager(list);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка!");
        }
    }

    protected void addToHistoryManager(List<Integer> list) {
        for (Integer integer : list) {
            for (Map.Entry<Integer, Task> integerTaskEntry : taskHashMap.entrySet()) {
                if (integerTaskEntry.getKey().equals(integer)) {
                    historyManager.add(getTask(integer));
                }
            }
            for (Map.Entry<Integer, Epic> integerTaskEntry : epicHashMap.entrySet()) {
                if (integerTaskEntry.getKey().equals(integer)) {
                    historyManager.add(getEpic(integer));
                }
            }
            for (Map.Entry<Integer, SubTask> integerTaskEntry : subTaskHashMap.entrySet()) {
                if (integerTaskEntry.getKey().equals(integer)) {
                    historyManager.add(getSubTask(integer));
                }
            }
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        manager.load();
        return manager;
    }
}

