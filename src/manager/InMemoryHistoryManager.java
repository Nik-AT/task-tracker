package manager;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyArray = new ArrayList<>();  // New arraylist for add object Task, Sub, Epic

    @Override
    public void add(Task task) {
        if (historyArray.size() >= 10) {
            historyArray.remove(0);
        }
        historyArray.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyArray;
    }
}
