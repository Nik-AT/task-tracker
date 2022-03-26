package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTaskArray = new ArrayList<>();

    public Epic(String name, String description, String status, int id) {  // если создана эпик без подзадач
        super(name, description, status, id);
    }

    public Epic(String name, String description, String status, int id, ArrayList<SubTask> subTaskArray) {
        super(name, description, status, id);
        this.subTaskArray = subTaskArray;
    }

    public ArrayList<SubTask> getSubTaskArray() {
        return subTaskArray;
    }

    public void setSubTaskArray(ArrayList<SubTask> subTaskArray) {
        this.subTaskArray = subTaskArray;
    }

    @Override
    public String toString() {
        return "Название ЭПИК: " + getName() + "\n" + "Описание ЭПИК: " + getDescription()
                + "\n" + "Статус ЭПИК: " + getStatus() + "\n" + "ID: " + getId() + "\n"
                + "Подзадачи :" + "\n" + subTaskArray;

    }

}
