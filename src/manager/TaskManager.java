package manager;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    /**
     * 1.1
     * Получение списка всех задач
     */
    List<Task> getTask();

    /**
     * 1.2
     * Удаление всех задач
     */
    void deleteTask();

    /**
     * 1.3
     * Получение задачи по идентификатору
     */
    Task takeTask(int id);

    /**
     * 1.4
     * Создание задач. Сам объект должен передаваться в качестве параметра
     */
    void createTask(Task task);

    /**
     * 1.5
     * Обновление задачи
     */
    void updateTask(Task task);

    /**
     * 1.6
     * Удаление по идентификатору
     */
    void removeTask(int id);

    /**
     * 2.1
     * Получение списка всех подзадач
     */
    ArrayList<SubTask> getSubTask();

    /**
     * 2.2
     * Удаление всех подзадач
     */
    void deleteSubTask();

    /**
     * 2.3
     * Получение задачи по идентификатору
     */
    SubTask takeSubTask(int id);

    /**
     * 2.4
     * Создание подзадач
     */
    SubTask createSubTask(SubTask subTask);

    /**
     * 2.5
     * Обновление подзадачи
     */
    void updateSubTask(SubTask subTask);

    /**
     * 2.6
     * Удаление подзадач по идентификатору
     */
    void removeSubTask(int id);

    /**
     * 3.1
     * Получение списка всех Эпиков
     */
    ArrayList<Epic> getEpic();

    /**
     * 3.2
     * Удаление всех Эпиков
     */
    void deleteEpic();

    /**
     * 3.3
     * Получение Эпиков по идентификатору
     */
    Epic takeEpic(int id);

    /**
     * 3.4
     * Создание Эпиков
     */
    Epic createEpic(Epic epic);

    /**
     * 3.5
     * Обновление Эпика
     */
    void updateEpic(Epic epic);

    /**
     * 3.6
     * Удаление Эпика по идентификатору
     */
    void deleteEpic(int id);

    /**
     * Возвращает историю задач
     */
    List<Task> getHistory();
}
