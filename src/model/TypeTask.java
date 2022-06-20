package model;

public enum TypeTask {
    TASK,
    SUBTASK,
    EPIC;


    public static TypeTask getType(String typeString) {
        switch (typeString) {
            case "task":
                return TASK;
            case "subtask":
                return SUBTASK;
            case "epic":
                return EPIC;
            default:
                return TASK;
        }
    }

    public static String getTypeString(TypeTask typeTask) {
        switch (typeTask) {
            case TASK:
                return "task";
            case SUBTASK:
                return "subtask";
            case EPIC:
                return "epic";
            default:
                return "task";
        }
    }
}
