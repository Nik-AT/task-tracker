package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int generatorId = 0;
    private final HashMap<Integer, Task> taskHashMap;
    private final HashMap<Integer, SubTask> subTaskHashMap;
    private final HashMap<Integer, Epic> epicHashMap;
    private final HistoryManager historyManager = Managers.getHistory();  // new InMemoryHistoryManager

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
    public ArrayList<Task> getTask() {
        return new ArrayList<>(taskHashMap.values());
    }

    /**
     * 1.2
     * Удаление всех задач
     */
    @Override
    public void deleteTask() {
        taskHashMap.clear();
    }

    /**
     * 1.3
     * Получение задачи по идентификатору
     */
    @Override
    public Task takeTask(int id) {
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
    }

    /**
     * 2.1
     * Получение списка всех подзадач
     */
    @Override
    public ArrayList<SubTask> getSubTask() {
        return new ArrayList<>(subTaskHashMap.values());
    }

    /**
     * 2.2
     * Удаление всех подзадач
     */
    @Override
    public void deleteSubTask() {
        subTaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.getSubTaskArray().clear();
            updateStatus(epic.getId());
        }
    }

    /**
     * 2.3
     * Получение задачи по идентификатору
     */
    @Override
    public SubTask takeSubTask(int id) {
        historyManager.add(subTaskHashMap.get(id));
        return subTaskHashMap.get(id);
    }

    /**
     * 2.4
     * Создание подзадач
     */
    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(++generatorId);
        Epic epic = epicHashMap.get(subTask.getEpicId());
        epic.getSubTaskArray().add(subTask);
        subTaskHashMap.put(subTask.getId(), subTask);
        updateStatus(subTask.getEpicId());
        return subTask;
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
    }

    /**
     * 3.1
     * Получение списка всех Эпиков
     */
    @Override
    public ArrayList<Epic> getEpic() {
        return new ArrayList<>(epicHashMap.values());
    }

    /**
     * 3.2
     * Удаление всех Эпиков
     */
    @Override
    public void deleteEpic() {
        for (Integer key : epicHashMap.keySet()) {
            epicHashMap.get(key).getSubTaskArray().clear();
        }
        epicHashMap.clear();
    }

    /**
     * 3.3
     * Получение Эпиков по идентификатору
     */
    @Override
    public Epic takeEpic(int id) {
        historyManager.add(epicHashMap.get(id));
        return epicHashMap.get(id);
    }

    /**
     * 3.4
     * Создание Эпиков
     */
    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(++generatorId);
        epicHashMap.put(epic.getId(), epic);
        return epic;
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
    public void deleteEpic(int id) {
        Epic currentEpic = epicHashMap.remove(id);
        for (SubTask subTask : currentEpic.getSubTaskArray()) {
            subTaskHashMap.remove(subTask.getId());
        }
    }

    /**
     * 4.0
     * Получение списка всех подзадач Эпик
     */
    public ArrayList<SubTask> getSubTaskOfEpic(Epic epic) {
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





