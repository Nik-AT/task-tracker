package model;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, Status status, int id) {
        super(name, description, status, id);
        this.epicId = id;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
