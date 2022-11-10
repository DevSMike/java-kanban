package service;

public class StatusManager {
    public enum Statuses {
        NEW,
        IN_PROGRESS,
        DONE
    }
    public static String getStatusStringById(Statuses status) {
        return status.name();
    }

    public static Statuses getStatusIdByString(String status) {
        return Statuses.valueOf(status);
    }
}
