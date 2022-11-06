package service;

public class StatusManager {
    public enum Statuses {
        NEW,
        IN_PROGRESS,
        DONE
    }
    public static String getStatusId(Statuses status) {
        switch(status) {
            case NEW:
                return "NEW";
            case IN_PROGRESS:
                return "IN_PROGRESS";
            case DONE:
                return "DONE";
            default: return "NULL";
        }
    }
}
