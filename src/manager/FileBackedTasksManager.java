package manager;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static model.TypeTask.SUBTASK;
import static model.TypeTask.TASK;

public class FileBackedTasksManager extends InMemoryTaskManager {

    public static void main(String[] args) {
        saveToFile();
        loadFromFile();
    }
    private final File file;

    public FileBackedTasksManager(File file) {
        this(file, false);
    }

    public FileBackedTasksManager(File file, boolean load) {
        this.file = file;
        if (load) {
            load();
        }
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
    public ArrayList<SubTask> getAllSubTaskOfEpic(Epic epic) {
        return super.getAllSubTaskOfEpic(epic);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = super.getHistory();
        save();
        return history;
    }

    private void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append("id,type,name,status,description,epic");
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
            //todo
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String toString(Task task) {
        String taskString = task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription() + ",";
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
        switch (typeTask) {
            case TASK:
                return new Task(name, description, status, id);
            case SUBTASK:
                return new SubTask(name, description, status, id, Integer.parseInt(split[5]));
            case EPIC:
                return new Epic(name, description, status, id);
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

    private void load() {
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
            loadFromFile(list);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка!");
        }
    }

    private void loadFromFile(List<Integer> list) {
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



    private static void saveToFile() {
        FileBackedTasksManager fb = new FileBackedTasksManager(new File("task.csv"));

        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW, 0);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW, 0);
        Task task3 = new Task("Задача2", "Описание задачи2", Status.NEW, 0);

        Epic epic1 = new Epic("Эпик1", "Описание ЭПИК1", Status.NEW, 0);
        Epic epic2 = new Epic("Эпик2", "Описание ЭПИК2", Status.NEW, 0);

        SubTask subTask1 = new SubTask("Саб для эпика1", "Описание подзадачи1", Status.DONE, 0, 4);
        SubTask subTask2 = new SubTask("Саб для эпика2", "Описание подзадачи2", Status.NEW, 0, 4);
        SubTask subTask3 = new SubTask("Саб для эпика3", "Описание подзадачи3", Status.NEW, 0, 5);

        fb.createTask(task1);
        fb.createTask(task2);
        fb.createTask(task3);
        fb.createEpic(epic1);
        fb.createEpic(epic2);
        fb.createSubTask(subTask1);
        fb.createSubTask(subTask2);
        fb.createSubTask(subTask3);
        fb.getTask(1);
        fb.getSubTask(6);
        fb.getSubTask(7);
        fb.getEpic(4);
        fb.getEpic(5);
        fb.getHistory();
    }

    private static void loadFromFile() {
        FileBackedTasksManager.loadFromFile(new File("task.csv"));
        TaskManager inMemoryTaskManager = FileBackedTasksManager.loadFromFile(new File("task.csv"));
        System.out.println("Задачи:");
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println("\n" + "Эпики:");
        System.out.println(inMemoryTaskManager.getAllEpic());
        System.out.println("\n" + "Подзадачи:");
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println("\n" + "История из файла:");
        System.out.println(inMemoryTaskManager.getHistory());
    }
}

