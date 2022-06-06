package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class ManagerTest<T extends TaskManager> {
    protected T inMemoryTaskManager;
    Task task;
    Epic epic;
    SubTask subTask;

    void init() {
        task = new Task("Задача1", "Описание задачи1", Status.NEW, 0, LocalDateTime.now(), 0);
        epic = new Epic("Эпик1", "Описание ЭПИК1", Status.NEW, 0, LocalDateTime.now(), 1);
        subTask = new SubTask("Саб для эпика1", "Описание подзадачи1", Status.DONE, 0, LocalDateTime.now(), 0, 2);
    }

    @Test
    void getAllTasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        assertEquals(0, taskManager.taskHashMap.size());
        inMemoryTaskManager.createTask(task);
        List<Task> allTasks = inMemoryTaskManager.getAllTasks();
        assertEquals(1, allTasks.size(), "Не верное количество");
    }

    @Test
    void deleteAllTasks() {
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.deleteAllTasks();
        List<Task> allTasks = inMemoryTaskManager.getAllTasks();
        assertEquals(0, allTasks.size());
    }

    @Test
    void getTask() {
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.getTask(task.getId());
        List<Task> allTasks = inMemoryTaskManager.getAllTasks();
        assertNotNull(allTasks);
        assertEquals(task.getId(), allTasks.get(0).getId());
        assertEquals("Задача1", allTasks.get(0).getName());
    }

    @Test
    void createTask() {
        inMemoryTaskManager.createTask(task);
        List<Task> allTasks = inMemoryTaskManager.getAllTasks();
        assertNotNull(allTasks);
        assertEquals(task.getId(), allTasks.get(0).getId());
        assertEquals("Задача1", allTasks.get(0).getName());
    }

    @Test
    void updateTask() {
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW, 0, LocalDateTime.now().plusMinutes(10), 0);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.updateTask(task);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.updateTask(task2);
        List<Task> allTasks = inMemoryTaskManager.getAllTasks();
        assertNotNull(allTasks);
        assertEquals(1, allTasks.get(0).getId());
        assertEquals("Задача1", allTasks.get(0).getName());
        assertEquals(task2.getId(), allTasks.get(1).getId());
    }

    @Test
    void removeTask() {
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.removeTask(task.getId());
        List<Task> allTasks = inMemoryTaskManager.getAllTasks();
        assertNotEquals(1, allTasks.size());
        assertEquals(0, allTasks.size());
    }

    @Test
    void getAllSubTasks() {
        inMemoryTaskManager.createEpic(epic);
        subTask.setEpicId(epic.getId());
        inMemoryTaskManager.createSubTask(subTask);
        ArrayList<SubTask> allSubTasks = inMemoryTaskManager.getAllSubTasks();
        assertEquals(1, allSubTasks.size());
        assertNotNull(allSubTasks);
    }

    @Test
    void deleteAllSubTasks() {
        inMemoryTaskManager.createEpic(epic);
        subTask.setEpicId(epic.getId());
        inMemoryTaskManager.createSubTask(subTask);
        inMemoryTaskManager.deleteAllSubTasks();
        ArrayList<SubTask> allSubTasks = inMemoryTaskManager.getAllSubTasks();
        assertEquals(0, allSubTasks.size());
    }

    @Test
    void getSubTask() {
        inMemoryTaskManager.createEpic(epic);
        subTask.setEpicId(epic.getId());
        inMemoryTaskManager.createSubTask(subTask);
        SubTask subTask = inMemoryTaskManager.getSubTask(this.subTask.getId());
        assertEquals("Саб для эпика1", subTask.getName());
    }

    @Test
    void createSubTask() {
        inMemoryTaskManager.createEpic(epic);
        subTask.setEpicId(epic.getId());
        inMemoryTaskManager.createSubTask(subTask);
        ArrayList<SubTask> allSubTasks = inMemoryTaskManager.getAllSubTasks();
        assertEquals(1, allSubTasks.size());
    }

    @Test
    void updateSubTask() {
        inMemoryTaskManager.createEpic(epic);
        subTask.setEpicId(epic.getId());
        inMemoryTaskManager.createSubTask(subTask);
        subTask.setStatus(Status.NEW);
        inMemoryTaskManager.updateSubTask(subTask);
        assertEquals(Status.NEW, subTask.getStatus());
        subTask.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubTask(subTask);
        assertEquals(Status.IN_PROGRESS, subTask.getStatus());
    }

    @Test
    void removeSubTask() {
        inMemoryTaskManager.createEpic(epic);
        subTask.setEpicId(epic.getId());
        inMemoryTaskManager.createSubTask(subTask);
        inMemoryTaskManager.removeSubTask(subTask.getId());
        ArrayList<SubTask> allSubTasks = inMemoryTaskManager.getAllSubTasks();
        assertEquals(0, allSubTasks.size());
    }

    @Test
    void getAllEpic() {
        Epic epic2 = new Epic("Эпик2", "Описание ЭПИК2", Status.NEW, 0, LocalDateTime.now(), 15);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createEpic(epic2);
        ArrayList<Epic> allEpic = inMemoryTaskManager.getAllEpic();
        assertEquals(2, allEpic.size());
        assertEquals("Эпик1", allEpic.get(0).getName());
        assertEquals("Эпик2", allEpic.get(1).getName());
    }

    @Test
    void deleteAllEpic() {
        Epic epic2 = new Epic("Эпик2", "Описание ЭПИК2", Status.NEW, 0, LocalDateTime.now(), 15);
        inMemoryTaskManager.createEpic(epic);
        subTask.setEpicId(epic.getId());
        inMemoryTaskManager.createSubTask(subTask);
        inMemoryTaskManager.createEpic(epic2);
        inMemoryTaskManager.deleteAllEpic();
        ArrayList<Epic> allEpic = inMemoryTaskManager.getAllEpic();
        ArrayList<SubTask> allSubTasks = inMemoryTaskManager.getAllSubTasks();
        assertEquals(0, allEpic.size());
        assertEquals(0, allSubTasks.size());
    }

    @Test
    void getEpic() {
        inMemoryTaskManager.createEpic(epic);
        Epic epic = inMemoryTaskManager.getEpic(this.epic.getId());
        ArrayList<Epic> allEpic = inMemoryTaskManager.getAllEpic();
        assertNotNull(allEpic);
        assertEquals(this.epic.getId(), epic.getId());
        assertEquals("Эпик1", epic.getName());
    }

    @Test
    void createEpic() {
        inMemoryTaskManager.createEpic(epic);
        ArrayList<Epic> allEpic = inMemoryTaskManager.getAllEpic();
        assertEquals(1, allEpic.size());
        assertEquals("Эпик1", allEpic.get(0).getName());
    }

    @Test
    void updateEpic() {
        Epic epic2 = new Epic("Эпик2", "Описание ЭПИК2", Status.NEW, 0, LocalDateTime.now(), 15);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.updateEpic(epic);
        inMemoryTaskManager.createEpic(epic2);
        inMemoryTaskManager.updateEpic(epic2);
        ArrayList<Epic> allEpic = inMemoryTaskManager.getAllEpic();
        assertNotNull(allEpic);
        assertEquals(1, allEpic.get(0).getId());
        assertEquals("Эпик1", allEpic.get(0).getName());
        assertEquals(2, allEpic.get(1).getId());
    }

    @Test
    void removeEpic() {
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.removeEpic(epic.getId());
        ArrayList<Epic> allEpic = inMemoryTaskManager.getAllEpic();
        assertEquals(0, allEpic.size());
    }

    @Test
    void removeEpicMustDeleteSubTaskThisEpic() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.createEpic(epic);
        subTask.setEpicId(epic.getId());
        inMemoryTaskManager.createSubTask(subTask);
        inMemoryTaskManager.removeEpic(epic.getId());
        SubTask subTask = inMemoryTaskManager.subTaskHashMap.get(this.subTask.getId());
        assertNull(subTask, "Саб не удалился" + subTask);
    }

    @Test
    void getAllSubTaskOfEpic() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.createEpic(epic);
        subTask.setEpicId(epic.getId());
        inMemoryTaskManager.createSubTask(subTask);
        ArrayList<SubTask> allSubTaskOfEpic = inMemoryTaskManager.getAllSubTaskOfEpic(epic);
        assertEquals("Саб для эпика1", allSubTaskOfEpic.get(0).getName());
    }

    @Test
    void getHistory() {
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.getEpic(epic.getId());
        List<Task> history = inMemoryTaskManager.getHistory();
        assertEquals(2, history.size());
    }
}