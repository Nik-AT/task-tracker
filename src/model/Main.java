package model;

import manager.Manager;

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
        Manager manager = new Manager();

        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW, 0);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW, 0);
        Task task3 = new Task("Задача3", "Описание задачи3", Status.NEW, 0);

        Epic epic1 = new Epic("Эпик1", "Описание ЭПИК1", Status.NEW, 0);
        Epic epic2 = new Epic("Эпик2", "Описание ЭПИК2", Status.NEW, 0);

        SubTask subTask1 = new SubTask("Саб для эпика1", "Описание подзадачи1", Status.NEW, 0);
        SubTask subTask2 = new SubTask("Саб для эпика2", "Описание подзадачи2", Status.NEW, 0);
        SubTask subTask3 = new SubTask("Саб для эпика3", "Описание подзадачи3", Status.NEW, 0);

        System.out.println("Список эпиков: ");
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        System.out.println(manager.getEpic());
        System.out.println("");
        System.out.println("Список подзадач: ");
        subTask1.setEpicId(epic1.getId());
        manager.createSubTask(subTask1);
        subTask2.setEpicId(epic1.getId());
        manager.createSubTask(subTask2);
        subTask3.setEpicId(epic2.getId());
        manager.createSubTask(subTask3);
        System.out.println(manager.getSubTask());
        System.out.println("");
        System.out.println("Список задач: ");
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        System.out.println(manager.getTask() + "\n");
        System.out.println("Изменение статуса задач:");
        task1.setStatus(Status.DONE);
        task2.setStatus(Status.DONE);
        task3.setStatus(Status.DONE);
        System.out.println(manager.getTask() + "\n");
        System.out.println("Изменение статуса подзадач:");
        subTask1.setStatus(Status.DONE);
        manager.updateSubTask(subTask1);
        subTask2.setStatus(Status.NEW);
        manager.updateSubTask(subTask2);
        System.out.println("subTask1.Status: " + subTask1.getStatus() + "\n" + "subTask2.Status:  " + subTask2.getStatus());
        System.out.println("");
        System.out.println("Статус эпика:");
        System.out.println(epic1.getStatus() + "\n");
        System.out.println("Удаление подзадачи :");
        manager.removeSubTask(subTask1.getId());
        System.out.println(epic1.getSubTaskArray() + "\n");
        System.out.println("Удаление эпика :");
        manager.deleteEpic(epic2.getId());
        System.out.println(manager.getEpic());
    }
}
