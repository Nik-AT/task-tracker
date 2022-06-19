package model;

public enum Status {
    NEW,
    IN_PROGRESS,
    DONE;

    public static Status getStatus(String statusString) {
        switch (statusString) {
            case "new":
                return NEW;
            case "in_progress":
                return IN_PROGRESS;
            case "done":
                return DONE;
            default:
                return NEW;
        }
    }

    public static String getStatusString(Status status) {
        switch (status) {
            case NEW:
                return "new";
            case IN_PROGRESS:
                return "in_progress";
            case DONE:
                return "done";
            default:
                return "new";
        }
    }
}
