package fcu.ms.data;

import java.time.LocalDateTime;

public final class TaskBuilder {
    private final int taskID;
    private String name;
    private String message;
    private LocalDateTime startPostTime;
    private LocalDateTime endPostTime;
    private final int salary;
    private final int releaseUserID;
    private final LocalDateTime releaseTime;
    private int receiveUserID;
    private String taskAddress;

    private TaskBuilder(int taskID, int salary, int releaseUserID, LocalDateTime releaseTime) {
        this.taskID = taskID;
        this.salary = salary;
        this.releaseUserID = releaseUserID;
        this.releaseTime = releaseTime;
    }

    public static TaskBuilder aTask(int taskID, int salary, int releaseUserID, LocalDateTime releaseTime) {
        return new TaskBuilder(taskID, salary, releaseUserID, releaseTime);
    }

    public TaskBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TaskBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public TaskBuilder withStartPostTime(LocalDateTime startPostTime) {
        this.startPostTime = startPostTime;
        return this;
    }

    public TaskBuilder withEndPostTime(LocalDateTime endPostTime) {
        this.endPostTime = endPostTime;
        return this;
    }


    public TaskBuilder withReceiveUserID(int receiveUserID) {
        this.receiveUserID = receiveUserID;
        return this;
    }

    public TaskBuilder withTaskAddress(String taskAddress) {
        this.taskAddress = taskAddress;
        return this;
    }

    public Task build() {
        Task task = new Task();
        task.setTaskID(taskID);
        task.setName(name);
        task.setMessage(message);
        task.setStartPostTime(startPostTime);
        task.setEndPostTime(endPostTime);
        task.setSalary(salary);
        task.setReleaseUserID(releaseUserID);
        task.setReleaseTime(releaseTime);
        task.setReceiveUserID(receiveUserID);
        task.setTaskAddress(taskAddress);
        return task;
    }
}
