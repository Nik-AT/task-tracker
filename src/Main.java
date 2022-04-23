import manager.*;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

public class Main {
    public static void main(String[] args) {

        TaskManager inMemoryTaskManager = Managers.getInMemoryTask() ;  //  new InMemoryTaskManager();
        HistoryManager historyManager = Managers.getHistory();

        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW, 0);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW, 0);
        Task task3 = new Task("Задача3", "Описание задачи3", Status.NEW, 0);

        Epic epic1 = new Epic("Эпик1", "Описание ЭПИК1", Status.NEW, 0);
        Epic epic2 = new Epic("Эпик2", "Описание ЭПИК2", Status.NEW, 0);

        SubTask subTask1 = new SubTask("Саб для эпика1", "Описание подзадачи1", Status.NEW, 0);
        SubTask subTask2 = new SubTask("Саб для эпика2", "Описание подзадачи2", Status.NEW, 0);
        SubTask subTask3 = new SubTask("Саб для эпика3", "Описание подзадачи3", Status.NEW, 0);

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic2);
        subTask1.setEpicId(epic1.getId());
        inMemoryTaskManager.createSubTask(subTask1);
        subTask2.setEpicId(epic1.getId());
        inMemoryTaskManager.createSubTask(subTask2);
        subTask3.setEpicId(epic1.getId());
        inMemoryTaskManager.createSubTask(subTask3);
        System.out.println("*** Получение истории задач :");
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getTask(2);
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getTask(3);
        inMemoryTaskManager.getSubTask(6);
        inMemoryTaskManager.getSubTask(7);
        inMemoryTaskManager.getSubTask(8);
        inMemoryTaskManager.getEpic(4);
        System.out.println(historyManager.getHistory());
        System.out.println("***END***");

        System.out.println("*** Удаление 1, 2, 3 задачи :");
        inMemoryTaskManager.removeTask(2);
        inMemoryTaskManager.removeTask(1);
        inMemoryTaskManager.removeTask(3);
        System.out.println(historyManager.getHistory());
        System.out.println("***END***");

        System.out.println("Удаление эпика :");
        inMemoryTaskManager.deleteAllEpic(4);
        System.out.println(historyManager.getHistory());
        System.out.println("***END***");
    }
}
