import manager.*;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.List;

public class Main {
    public static void main(String[] args) {
/**
 * Тест
 *
 * Создайте 3 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
 * Распечатайте списки эпиков, задач и подзадач, через System.out.println(..)
 * Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи
 * сохранился, а статус эпика рассчитался по статусам подзадач.
 * И, наконец, попробуйте удалить одну из задач и один из эпиков.
 */
        TaskManager inMemoryTaskManager = Managers.getInMemoryTask() ;
        List<Task> history = inMemoryTaskManager.getHistory();

        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW, 0);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW, 0);
        Task task3 = new Task("Задача3", "Описание задачи3", Status.NEW, 0);
        Task task4 = new Task("Задача4", "Описание задачи1", Status.NEW, 0);
        Task task5 = new Task("Задача5", "Описание задачи1", Status.NEW, 0);
        Task task6 = new Task("Задача6", "Описание задачи1", Status.NEW, 0);
        Task task7 = new Task("Задача7", "Описание задачи1", Status.NEW, 0);
        Task task8 = new Task("Задача8", "Описание задачи1", Status.NEW, 0);
        Task task9 = new Task("Задача9", "Описание задачи1", Status.NEW, 0);
        Task task10 = new Task("Задач10", "Описание задачи1", Status.NEW, 0);
        Task task11 = new Task("Задача11", "Описание задачи1", Status.NEW, 0);
        Task task12 = new Task("Задача12", "Описание задачи1", Status.NEW, 0);
        Task task13 = new Task("Задача13", "Описание задачи1", Status.NEW, 0);
        Task task14 = new Task("Задача14", "Описание задачи1", Status.NEW, 0);
        Task task15 = new Task("Задача15", "Описание задачи1", Status.NEW, 0);
        Task task16 = new Task("Задача16", "Описание задачи1", Status.NEW, 0);

        Epic epic1 = new Epic("Эпик1", "Описание ЭПИК1", Status.NEW, 0);
        Epic epic2 = new Epic("Эпик2", "Описание ЭПИК2", Status.NEW, 0);

        SubTask subTask1 = new SubTask("Саб для эпика1", "Описание подзадачи1", Status.NEW, 0);
        SubTask subTask2 = new SubTask("Саб для эпика2", "Описание подзадачи2", Status.NEW, 0);
        SubTask subTask3 = new SubTask("Саб для эпика3", "Описание подзадачи3", Status.NEW, 0);

        System.out.println("Список задач: ");
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.createTask(task4);
        inMemoryTaskManager.createTask(task5);
        inMemoryTaskManager.createTask(task6);
        inMemoryTaskManager.createTask(task7);
        inMemoryTaskManager.createTask(task8);
        inMemoryTaskManager.createTask(task9);
        inMemoryTaskManager.createTask(task10);
        inMemoryTaskManager.createTask(task11);
        inMemoryTaskManager.createTask(task12);
        inMemoryTaskManager.createTask(task13);
        inMemoryTaskManager.createTask(task14);
        inMemoryTaskManager.createTask(task15);
        inMemoryTaskManager.createTask(task16);
        System.out.println(inMemoryTaskManager.getTask() + "\n");
        System.out.println("Список эпиков: ");
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic2);
        System.out.println(inMemoryTaskManager.getEpic());
        System.out.println("");
        System.out.println("Список подзадач: ");
        subTask1.setEpicId(epic1.getId());
        inMemoryTaskManager.createSubTask(subTask1);
        subTask2.setEpicId(epic1.getId());
        inMemoryTaskManager.createSubTask(subTask2);
        subTask3.setEpicId(epic2.getId());
        inMemoryTaskManager.createSubTask(subTask3);
        System.out.println(inMemoryTaskManager.getSubTask());
        System.out.println("");
        System.out.println("Изменение статуса задач:");
        task1.setStatus(Status.DONE);
        task2.setStatus(Status.DONE);
        task3.setStatus(Status.DONE);
        System.out.println(inMemoryTaskManager.getTask() + "\n");
        System.out.println("Изменение статуса подзадач:");
        subTask1.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubTask(subTask1);
        subTask2.setStatus(Status.NEW);
        inMemoryTaskManager.updateSubTask(subTask2);
        System.out.println("subTask1.Status: " + subTask1.getStatus() + "\n" + "subTask2.Status:  "
                + subTask2.getStatus());
        System.out.println("");
        System.out.println("Статус эпика:");
        System.out.println(epic1.getStatus() + "\n");
        System.out.println("Удаление подзадачи :");
        inMemoryTaskManager.removeSubTask(subTask1.getId());
        System.out.println(epic1.getSubTaskArray() + "\n");
        System.out.println("Удаление эпика :");
        inMemoryTaskManager.deleteEpic(epic2.getId());
        System.out.println(inMemoryTaskManager.getEpic());
        System.out.println("**************");
        System.out.println(inMemoryTaskManager.getTask());
        inMemoryTaskManager.takeTask(1);
        inMemoryTaskManager.takeTask(2);
        inMemoryTaskManager.takeTask(3);
        inMemoryTaskManager.takeTask(4);
        inMemoryTaskManager.takeTask(5);
        inMemoryTaskManager.takeTask(6);
        inMemoryTaskManager.takeTask(7);
        inMemoryTaskManager.takeTask(8);
        inMemoryTaskManager.takeTask(9);
        inMemoryTaskManager.takeTask(10);
        inMemoryTaskManager.takeTask(11);
        inMemoryTaskManager.takeTask(12);
        inMemoryTaskManager.takeSubTask(20);
        inMemoryTaskManager.takeEpic(17);
        System.out.println("Получение истории последних 10 задач");
        System.out.println(history);
    }
}
