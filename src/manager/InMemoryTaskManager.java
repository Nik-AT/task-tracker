package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    protected int generatorId = 0;
    public final HashMap<Integer, Task> taskHashMap;
    protected final HashMap<Integer, SubTask> subTaskHashMap;
    protected final HashMap<Integer, Epic> epicHashMap;
    protected final HistoryManager historyManager = Managers.getHistory();  // new InMemoryHistoryManager

    public InMemoryTaskManager() {
        this.taskHashMap = new HashMap<>();
        this.subTaskHashMap = new HashMap<>();
        this.epicHashMap = new HashMap<>();
    }

    /**
     * 1.1
     * Получение списка всех задач
     */
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskHashMap.values());
    }

    /**
     * 1.2
     * Удаление всех задач
     */
    @Override
    public void deleteAllTasks() {
        for (Integer key : taskHashMap.keySet())
            historyManager.remove(key);
        taskHashMap.clear();
    }

    /**
     * 1.3
     * Получение задачи по идентификатору
     */
    @Override
    public Task getTask(int id) {
        historyManager.add(taskHashMap.get(id));  // Добавление задачи в список просмотров
        return taskHashMap.get(id);
    }


    /**
     * 1.4
     * Создание задач. Сам объект должен передаваться в качестве параметра
     */
    @Override
    public void createTask(Task task) {
        task.setId(++generatorId);
        taskHashMap.put(task.getId(), task);
    }

    /**
     * 1.5
     * Обновление задачи
     */
    @Override
    public void updateTask(Task task) {
        if (!taskHashMap.containsKey(task.getId())) {
            return;
        }
        taskHashMap.put(task.getId(), task);
    }

    /**
     * 1.6
     * Удаление по идентификатору
     */
    @Override
    public void removeTask(int id) {
        taskHashMap.remove(id);
        historyManager.remove(id);

    }

    /**
     * 2.1
     * Получение списка всех подзадач
     */
    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTaskHashMap.values());
    }

    /**
     * 2.2
     * Удаление всех подзадач
     */
    @Override
    public void deleteAllSubTasks() {
        subTaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.getSubTaskArray().clear();
            historyManager.remove(epic.getId());
            updateStatus(epic.getId());
        }
    }

    /**
     * 2.3
     * Получение задачи по идентификатору
     */
    @Override
    public SubTask getSubTask(int id) {
        historyManager.add(subTaskHashMap.get(id));
        return subTaskHashMap.get(id);
    }

    /**
     * 2.4
     * Создание подзадач
     */
    @Override
    public void createSubTask(SubTask subTask) {
        subTask.setId(++generatorId);
        Epic epic = epicHashMap.get(subTask.getEpicId());
        epic.getSubTaskArray().add(subTask);
        subTaskHashMap.put(subTask.getId(), subTask);
        updateStatus(subTask.getEpicId());
    }

    /**
     * 2.5
     * Обновление подзадачи
     */
    @Override
    public void updateSubTask(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(), subTask);
        updateStatus(subTask.getEpicId());
    }

    /**
     * 2.6
     * Удаление подзадач по идентификатору
     */
    @Override
    public void removeSubTask(int id) {
        SubTask subTask = subTaskHashMap.remove(id);
        Epic epic = epicHashMap.get(subTask.getEpicId());
        epic.getSubTaskArray().remove(subTask);
        updateStatus(subTask.getEpicId());
        historyManager.remove(id);
    }

    /**
     * 3.1
     * Получение списка всех Эпиков
     */
    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicHashMap.values());
    }

    /**
     * 3.2
     * Удаление всех Эпиков
     */
    @Override
    public void deleteAllEpic() {
        for (Integer key : epicHashMap.keySet()) {
            epicHashMap.get(key).getSubTaskArray().clear();
            historyManager.remove(key);
        }
        epicHashMap.clear();
    }

    /**
     * 3.3
     * Получение Эпиков по идентификатору
     */
    @Override
    public Epic getEpic(int id) {
        historyManager.add(epicHashMap.get(id));
        return epicHashMap.get(id);
    }

    /**
     * 3.4
     * Создание Эпиков
     */
    @Override
    public void createEpic(Epic epic) {
        epic.setId(++generatorId);
        epicHashMap.put(epic.getId(), epic);

    }

    /**
     * 3.5
     * Обновление Эпика
     */
    @Override
    public void updateEpic(Epic epic) {
        if (!epicHashMap.containsKey(epic.getId())) {
            return;
        }
        Epic epicSave = epicHashMap.get(epic.getId());
        epicSave.setName(epic.getName());
        epicSave.setDescription(epic.getDescription());
    }

    /**
     * 3.6
     * Удаление Эпика по идентификатору
     */
    @Override
    public void removeEpic(int id) {
        Epic currentEpic = epicHashMap.remove(id);
        for (SubTask subTask : currentEpic.getSubTaskArray()) {
            subTaskHashMap.remove(subTask.getId());
            historyManager.remove(subTask.getId());
        }
        historyManager.remove(id);
    }

    /**
     * 4.0
     * Получение списка всех подзадач Эпик
     */
    public ArrayList<SubTask> getAllSubTaskOfEpic(Epic epic) {
        Epic currentEpic = epicHashMap.get(epic.getId());
        return currentEpic.getSubTaskArray();
    }

    /**
     * 4.1 Получение списка истории просмотра задач
     */
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    /**
     * 4.2 Универскльный метод обновления статуса у Эпиков
     *
     * @return
     */
    private Status updateStatus(int id) {
        int counterNEW = 0;
        int counterDONE = 0;
        Epic epicTemp = epicHashMap.get(id);
        ArrayList<SubTask> subTaskArrayList = epicTemp.getSubTaskArray();
        if (subTaskArrayList.isEmpty()) {
            return epicTemp.setStatus(Status.NEW);
        }
        for (SubTask subTask : subTaskArrayList) {
            if (subTask.getStatus().equals(Status.NEW)) {
                counterNEW++;
            } else if (subTask.getStatus().equals(Status.DONE)) {
                counterDONE++;
            }
            if (counterNEW == subTaskArrayList.size()) {
                return epicTemp.setStatus(Status.NEW);
            } else if (counterDONE == subTaskArrayList.size())
                return epicTemp.setStatus(Status.DONE);
        }
        return epicTemp.setStatus(Status.IN_PROGRESS);
    }
}





