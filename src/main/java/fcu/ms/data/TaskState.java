package fcu.ms.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class TaskState {

    private int id;
    private int taskID;
    private TaskStateEnum taskState;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime stepTime;

    public TaskState() {

    }


    // 拿整個save_tasks_state資料表用
    public TaskState(int id, int taskID, TaskStateEnum taskState, LocalDateTime stepTime) {
        this.id = id;
        this.taskID = taskID;
        this.taskState = taskState;
        this.stepTime = stepTime;
    }

    // 新增資料用 無 id
    public TaskState(int taskID, TaskStateEnum taskState, LocalDateTime stepTime) {
        this.taskID = taskID;
        this.taskState = taskState;
        this.stepTime = stepTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public TaskStateEnum getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskStateEnum taskState) {
        this.taskState = taskState;
    }

    public LocalDateTime getStepTime() {
        return stepTime;
    }

    public void setStepTime(LocalDateTime stepTime) {
        this.stepTime = stepTime;
    }

    @Override
    public String toString() {
        return "TaskState{" +
                "id=" + id +
                ", taskID=" + taskID +
                ", taskState=" + taskState +
                ", stepTime=" + stepTime +
                '}';
    }
}
