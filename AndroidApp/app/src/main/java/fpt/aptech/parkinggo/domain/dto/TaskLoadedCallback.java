package fpt.aptech.parkinggo.domain.dto;

public interface TaskLoadedCallback {
    void onTaskDone(Object... values);
}