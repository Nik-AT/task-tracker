package manager;

import model.Task;

import java.util.List;

public interface HistoryManager {

    /**
     * Помечает задачи как просмотренные
     */
    void add(Task task);

    /**
     * Возвращает историю задач
     */
    List<Task> getHistory();
}
