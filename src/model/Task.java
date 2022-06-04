package model;

import java.time.LocalDateTime;

public class Task {

    private String name;
    private String description;
    private Status status;
    private int id;
    private LocalDateTime startTime;
    private int duration;
    private LocalDateTime endTime;

    public Task(String name, String description, Status status, int id, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plusMinutes(duration);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public Status setStatus(Status status) {
        this.status = status;
        return status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public TypeTask getType() {
        return TypeTask.TASK;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    @Override
//    public String toString() {
//        return "Task{" +
//                "name='" + name + '\'' +
//                ", id=" + id +
//                '}';
//    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", startTime=" + startTime +
                '}';
    }
}

