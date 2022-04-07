package manager;

public class Managers {

    public static TaskManager getInMemoryTask() {

        return new InMemoryTaskManager();
    }

    public static HistoryManager getHistory() {

        return new InMemoryHistoryManager();
    }
}


