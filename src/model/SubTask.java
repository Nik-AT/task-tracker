package model;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, Status status, int id, int epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TypeTask getType() {
        return TypeTask.SUBTASK;
    }

}
