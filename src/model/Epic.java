package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTaskArray = new ArrayList<>();
    private LocalDateTime startTime;
    private int duration;
    private LocalDateTime endTime;

    public Epic(String name, String description, Status status, int id, LocalDateTime startTime, int duration) {  // если создана эпик без подзадач
        super(name, description, status, id, startTime, duration);
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plusMinutes(getDurationOfEpic());
    }

    public Epic(String name, String description, Status status, int id, ArrayList<SubTask> subTaskArray, LocalDateTime startTime, int duration) {
        super(name, description, status, id, startTime, duration);
        this.subTaskArray = subTaskArray;
    }

    public ArrayList<SubTask> getSubTaskArray() {
        return subTaskArray;
    }
    public int getDurationOfEpic() {
        int duration = 0;
        for (SubTask subTask : subTaskArray) {
           duration = duration + subTask.getDuration();
        }
        return duration;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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
