package fcu.ms.data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Task {
    private int taskID = -1;
    private String name;
    private String message;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startPostTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endPostTime;

    private int salary;
    private int releaseUserID;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseTime;

    private int receiveUserID = 0;

    private String taskAddress;

    private String content;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime messageSendTime;

    public Task() {
        // 需要這個做反序列化, 跟 import com.fasterxml.jackson.databind.ObjectMapper; 的 convertValue 有關
    }
    public Task(int id, String name, String content, LocalDateTime messageSendTime){
        this.taskID = id;
        this.name = name;
        this.content = content;
        this.messageSendTime = messageSendTime;
    }


    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getName() {
        return name;
    }

    public String getTaskAddress() {
        return taskAddress;
    }

    public void setTaskAddress(String taskAddress) {
        this.taskAddress = taskAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getStartPostTime() { return startPostTime; }

    public void setStartPostTime(LocalDateTime startPostTime) { this.startPostTime = startPostTime; }

    public LocalDateTime getEndPostTime() {
        return endPostTime;
    }

    public void setEndPostTime(LocalDateTime endPostTime) {
        this.endPostTime = endPostTime;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getReleaseUserID() {
        return releaseUserID;
    }

    public void setReleaseUserID(int releaseUserID) {
        this.releaseUserID = releaseUserID;
    }

    public LocalDateTime getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(LocalDateTime releaseTime) {
        this.releaseTime = releaseTime;
    }

    public int getReceiveUserID() {
        return receiveUserID;
    }

    public void setReceiveUserID(int receiveUserID) {
        this.receiveUserID = receiveUserID;
    }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public LocalDateTime getMessageSendTime() {
        return messageSendTime;
    }

    public void setMessageSendTime(LocalDateTime messageSendTime) {
        this.messageSendTime = messageSendTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", name='" + name +
                ", message='" + message +
                ", startPostTime=" + startPostTime +
                ", endPostTime=" + endPostTime +
                ", salary=" + salary +
                ", releaseUserID=" + releaseUserID +
                ", releaseTime=" + releaseTime +
                ", receiveUserID=" + receiveUserID +
                ", taskAddress='" + taskAddress +
                ", content='" + content +
                ", messageSendTime=" + messageSendTime +
                '}';
    }
}
