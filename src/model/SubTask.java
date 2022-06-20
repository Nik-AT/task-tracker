package model;

import java.time.LocalDateTime;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, Status status, int id, LocalDateTime startTime, int duration, int epicId) {
        super(name, description, status, id, startTime, duration);
        this.epicId = epicId;
        this.setTypeTask(TypeTask.SUBTASK);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }


}
