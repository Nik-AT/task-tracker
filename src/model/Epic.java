package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTaskArray = new ArrayList<>();

    public Epic(String name, String description, Status status, int id, LocalDateTime startTime, int duration) {  // если создана эпик без подзадач
        super(name, description, status, id, startTime, duration);
    }

    public Epic(String name, String description, Status status, int id, ArrayList<SubTask> subTaskArray, LocalDateTime startTime, int duration) {
        super(name, description, status, id, startTime, duration);
        this.subTaskArray = subTaskArray;
    }

    public ArrayList<SubTask> getSubTaskArray() {
        return subTaskArray;
    }

    public void setSubTaskArray(ArrayList<SubTask> subTaskArray) {
        this.subTaskArray = subTaskArray;
    }

    @Override
    public TypeTask getType() {
        return TypeTask.EPIC;
    }

    @Override
    public String toString() {
        return "Название ЭПИК: " + getName() + " " + "Описание ЭПИК: "
                + " " + "Статус ЭПИК: " + getStatus() + " " + "ID: " + getId() + " "
                + "Подзадачи :" + " " + subTaskArray;

    }
}
