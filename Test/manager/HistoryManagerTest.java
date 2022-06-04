package manager;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryManagerTest {

    @Test
    void add() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW, 0, LocalDateTime.now(), 1);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(5), 1);
        Task task3 = new Task("Задача2", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(10), 1);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task3.getId());
        List<Task> history = inMemoryTaskManager.historyManager.getHistory();
        assertEquals(task1.getName(), history.get(0).getName());
        assertEquals(task2.getName(), history.get(1).getName());
        assertEquals(task3.getName(), history.get(2).getName());
    }

    @Test
    void getHistory() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW, 0, LocalDateTime.now(), 1);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(5), 1);
        Task task3 = new Task("Задача2", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(10), 1);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task3.getId());
        List<Task> history = inMemoryTaskManager.historyManager.getHistory();
        assertEquals(3, history.size());
    }

    @Test
    void remove() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW, 0, LocalDateTime.now(), 1);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(5), 1);
        Task task3 = new Task("Задача2", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(10), 1);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task3.getId());
        inMemoryTaskManager.historyManager.remove(task1.getId());
        List<Task> history = inMemoryTaskManager.historyManager.getHistory();
        assertEquals(2, history.size());
    }

    @Test
    void emptyArrayOfHistoryMustReturn0() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        List<Task> history = inMemoryTaskManager.historyManager.getHistory();
        assertEquals(0, history.size());
    }

    @Test
    void duplicateArrayOfHistoryMustReturn2() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW, 0, LocalDateTime.now(), 1);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(5), 1);
        Task task3 = new Task("Задача2", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(10), 1);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task1.getId());
        List<Task> history = inMemoryTaskManager.historyManager.getHistory();
        assertEquals(2, history.size());
    }

    @Test
    void removeOneTaskInBeginningOfArrayOfHistoryMustReturnTask1AndTask2() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW, 0, LocalDateTime.now(), 1);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(5), 1);
        Task task3 = new Task("Задача3", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(10), 1);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task3.getId());
        inMemoryTaskManager.historyManager.remove(task1.getId());
        List<Task> history = inMemoryTaskManager.historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals("Задача2", history.get(0).getName());
        assertEquals("Задача3", history.get(1).getName());
    }

    @Test
    void removeOneTaskInMiddleOfArrayOfHistoryMustReturnTask1AndTask3() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW, 0, LocalDateTime.now(), 1);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(5), 1);
        Task task3 = new Task("Задача3", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(10), 1);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task3.getId());
        inMemoryTaskManager.historyManager.remove(task2.getId());
        List<Task> history = inMemoryTaskManager.historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals("Задача1", history.get(0).getName());
        assertEquals("Задача3", history.get(1).getName());
    }

    @Test
    void removeOneTaskInEndOfArrayOfHistoryMustReturnTask1AndTask2() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW, 0, LocalDateTime.now(), 1);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(5), 1);
        Task task3 = new Task("Задача3", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(10), 1);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task3.getId());
        inMemoryTaskManager.historyManager.remove(task3.getId());
        List<Task> history = inMemoryTaskManager.historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals("Задача1", history.get(0).getName());
        assertEquals("Задача2", history.get(1).getName());
    }
}