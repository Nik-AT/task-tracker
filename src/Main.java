import manager.*;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Эпик1", "Описание Эпик1", Status.NEW, 0, LocalDateTime.now(), 1);
        SubTask subTask1 = new SubTask("Саб1", "Описание саб1", Status.NEW, 0, LocalDateTime.of
                (2022,6,6,12,0), 60, 1);
        SubTask subTask2 = new SubTask("Саб1", "Описание саб1", Status.NEW, 0, LocalDateTime.of
                (2022,6,6,13,0), 10, 1);
        SubTask subTask3 = new SubTask("Саб1", "Описание саб1", Status.NEW, 0, LocalDateTime.of
                (2022,6,6,10,0), 10, 1);
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask2);
        inMemoryTaskManager.createSubTask(subTask3);
        System.out.println(epic1.getStartTime());
        System.out.println(epic1.getDuration());
        System.out.println(epic1.getEndTime());
    }
}
