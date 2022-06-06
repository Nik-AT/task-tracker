package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int generatorId = 0;
    public final HashMap<Integer, Task> taskHashMap;
    protected final HashMap<Integer, SubTask> subTaskHashMap;
    protected final HashMap<Integer, Epic> epicHashMap;
    protected final HistoryManager historyManager = Managers.getHistory();  // new InMemoryHistoryManager
    protected Set<Task> tasksAndSubtask = new TreeSet<>(Comparator.nullsLast(Comparator.comparing(Task::getStartTime)));

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
        for (Task value : taskHashMap.values()) {
            tasksAndSubtask.remove(value);
            historyManager.remove(value.getId());
        }
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
        checkCrossTask(task);
        tasksAndSubtask.add(task);
        taskHashMap.put(task.getId(), task);
    }

    /**
     * 1.5
     * Обновление задачи
     */
    @Override
    public void updateTask(Task task) {
        checkCrossTask(task);
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
        Task task = taskHashMap.get(id);
        tasksAndSubtask.remove(task);
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
        for (SubTask subTask : subTaskHashMap.values()) {
            tasksAndSubtask.remove(subTask);
            historyManager.remove(subTask.getId());
        }
        subTaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.getSubTaskArray().clear();
            historyManager.remove(epic.getId());
            updateStatus(epic.getId());
            updateTime(epic.getId());
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
        checkCrossTask(subTask);
        tasksAndSubtask.add(subTask);
        Epic epic = epicHashMap.get(subTask.getEpicId());
        epic.getSubTaskArray().add(subTask);
        subTaskHashMap.put(subTask.getId(), subTask);
        updateStatus(subTask.getEpicId());
        updateTime(subTask.getEpicId());
    }

    /**
     * 2.5
     * Обновление подзадачи
     */
    @Override
    public void updateSubTask(SubTask subTask) {
        checkCrossTask(subTask);
        subTaskHashMap.put(subTask.getId(), subTask);
        updateStatus(subTask.getEpicId());
        updateTime(subTask.getEpicId());
    }

    /**
     * 2.6
     * Удаление подзадач по идентификатору
     */
    @Override
    public void removeSubTask(int id) {
        SubTask subTask = subTaskHashMap.remove(id);
        tasksAndSubtask.remove(subTask);
        Epic epic = epicHashMap.get(subTask.getEpicId());
        epic.getSubTaskArray().remove(subTask);
        updateStatus(subTask.getEpicId());
        updateTime(subTask.getEpicId());
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
        for (SubTask subTask : subTaskHashMap.values()) {
            historyManager.remove(subTask.getId());
        }
        subTaskHashMap.clear();
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
            tasksAndSubtask.remove(subTask);
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
     * 4.2 Универсальный метод обновления статуса у Эпиков
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

    public void updateTime(int id) {
        int duration = 0;
        LocalDateTime start = LocalDateTime.MAX;
        Epic epicTemp = epicHashMap.get(id);
        ArrayList<SubTask> subTaskArrayList = epicTemp.getSubTaskArray();
        for (SubTask subTask : subTaskArrayList) {
            duration = duration + subTask.getDuration();
            LocalDateTime startTime = subTask.getStartTime();
            if (startTime.isBefore(start)) {
                start = startTime;
            }
        }
        epicTemp.setStartTime(start);
        epicTemp.setDuration(duration);
        epicTemp.setEndTime(epicTemp.getStartTime().plusMinutes(epicTemp.getDuration()));
    }

    public Set<Task> getPrioritizedTasks() {
        return tasksAndSubtask;
    }

    public void checkCrossTask(Task task) {
        if (task.getStartTime() == null) {
            LocalDateTime latestTaskTime = LocalDateTime.MIN;
            for (Task taskTemp : tasksAndSubtask) {
                if (taskTemp.getStartTime().plusMinutes(taskTemp.getDuration()).isAfter(latestTaskTime)) {
                    latestTaskTime = taskTemp.getStartTime().plusMinutes(taskTemp.getDuration());
                }
            }
            task.setStartTime(latestTaskTime.plusMinutes(5));
        }
        LocalDateTime timeStartTime = task.getStartTime();
        LocalDateTime timeEndTime = task.getStartTime().plusMinutes(task.getDuration());
        for (Task tempTask : tasksAndSubtask) {
            if ((timeStartTime.isAfter(tempTask.getStartTime()) && timeStartTime.isBefore(tempTask.getStartTime()
                    .plusMinutes(tempTask.getDuration()))) ||
                    (timeEndTime.isAfter(tempTask.getStartTime()) && timeStartTime.isBefore(tempTask.getStartTime()
                            .plusMinutes(tempTask.getDuration())))) {
                throw new RuntimeException("Ошибка, пересечения задач --> " + tempTask.getName());
            }
        }
    }
}


