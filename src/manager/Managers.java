package manager;

public class Managers {
    private static TaskManager taskManager;
    private static HistoryManager historyManager;

    public static TaskManager getInMemoryTask() {
        if (taskManager == null) {
        taskManager = new InMemoryTaskManager();
        }
        return taskManager;
    }

    public static HistoryManager getHistory() {
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }

        return historyManager;
    }
}


