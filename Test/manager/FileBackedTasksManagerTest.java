package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTasksManagerTest extends ManagerTest<FileBackedTasksManager> {
    @BeforeEach
    void initInMemoryTaskManager() {
        inMemoryTaskManager = new FileBackedTasksManager(new File("task2.csv"));
        init();
    }

    @Test
    void emptyArrayMustBeReturn0() {
        FileBackedTasksManager fb = new FileBackedTasksManager(new File("task2.csv"));
        List<Task> history = fb.historyManager.getHistory();
        assertEquals(0, history.size());
    }

    @Test
    void whenAddingTaskArrayNotEmpty() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File("task2.csv"));
        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW, 0,
                LocalDateTime.now(), 1);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW, 0,
                LocalDateTime.now().plusMinutes(5), 1);
        Task task3 = new Task("Задача2", "Описание задачи2", Status.NEW, 0,
                LocalDateTime.now().plusMinutes(10), 1);

        Epic epic1 = new Epic("Эпик1", "Описание ЭПИК1", Status.NEW, 0,
                LocalDateTime.now().plusMinutes(15), 1);
        Epic epic2 = new Epic("Эпик2", "Описание ЭПИК2", Status.NEW, 0,
                LocalDateTime.now().plusMinutes(20), 1);

        SubTask subTask1 = new SubTask("Саб для эпика1", "Описание подзадачи1", Status.DONE, 0,
                LocalDateTime.now().plusMinutes(25), 1, 4);
        SubTask subTask2 = new SubTask("Саб для эпика2", "Описание подзадачи2", Status.NEW, 0,
                LocalDateTime.now().plusMinutes(30), 1, 5);
        SubTask subTask3 = new SubTask("Саб для эпика3", "Описание подзадачи3", Status.NEW, 0,
                LocalDateTime.now().plusMinutes(40), 1, 5);
        fileBackedTasksManager.createTask(task1);
        fileBackedTasksManager.createTask(task2);
        fileBackedTasksManager.createTask(task3);
        fileBackedTasksManager.createEpic(epic1);
        fileBackedTasksManager.createEpic(epic2);
        fileBackedTasksManager.createSubTask(subTask1);
        fileBackedTasksManager.createSubTask(subTask2);
        fileBackedTasksManager.createSubTask(subTask3);
        assertNotNull(fileBackedTasksManager);
    }

    @Test
    void emptyArrayWithOutHistoryReturn0() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File("task3.csv"));
        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW, 0,
                LocalDateTime.now(), 1);
        fileBackedTasksManager.createTask(task1);
        List<Task> history = fileBackedTasksManager.historyManager.getHistory();
        assertEquals(0, history.size());
    }

    @Test
    void load() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File("task.csv"));

        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW, 0,
                LocalDateTime.now(), 1);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW, 0,
                LocalDateTime.now().plusMinutes(5), 1);
        Task task3 = new Task("Задача2", "Описание задачи2", Status.NEW, 0,
                LocalDateTime.now().plusMinutes(10), 1);

        Epic epic1 = new Epic("Эпик1", "Описание ЭПИК1", Status.NEW, 0,
                LocalDateTime.now().plusMinutes(15), 1);
        Epic epic2 = new Epic("Эпик2", "Описание ЭПИК2", Status.NEW, 0,
                LocalDateTime.now().plusMinutes(20), 15);

        SubTask subTask1 = new SubTask("Саб для эпика1", "Описание подзадачи1", Status.DONE, 0,
                LocalDateTime.now().plusMinutes(30), 1, 4);
        SubTask subTask2 = new SubTask("Саб для эпика2", "Описание подзадачи2", Status.NEW, 0,
                LocalDateTime.now().plusMinutes(35), 1, 5);
        SubTask subTask3 = new SubTask("Саб для эпика3", "Описание подзадачи3", Status.NEW, 0,
                LocalDateTime.now().plusMinutes(40), 1, 5);

        fileBackedTasksManager.createTask(task1);
        fileBackedTasksManager.createTask(task2);
        fileBackedTasksManager.createTask(task3);
        fileBackedTasksManager.createEpic(epic1);
        fileBackedTasksManager.createEpic(epic2);
        fileBackedTasksManager.createSubTask(subTask1);
        fileBackedTasksManager.createSubTask(subTask2);
        fileBackedTasksManager.createSubTask(subTask3);
        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getSubTask(6);
        fileBackedTasksManager.getSubTask(7);
        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getSubTask(7);
        fileBackedTasksManager.getEpic(4);
        fileBackedTasksManager.getEpic(5);
        fileBackedTasksManager.getHistory();
        FileBackedTasksManager.loadFromFile(new File("task.csv"));
        TaskManager inMemoryTaskManager = FileBackedTasksManager.loadFromFile(new File("task.csv"));
        List<Task> allTasks = inMemoryTaskManager.getAllTasks();
        assertEquals("Задача1", allTasks.get(0).getName());
        ArrayList<SubTask> allSubTasks = inMemoryTaskManager.getAllSubTasks();
        assertEquals("Саб для эпика1", allSubTasks.get(0).getName());
        ArrayList<Epic> allEpic = inMemoryTaskManager.getAllEpic();
        assertEquals("Эпик1", allEpic.get(0).getName());
    }
}