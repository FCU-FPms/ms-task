package fcu.ms.data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Message {
    private int id;
    private String content;
    private int userID;
    private int receiverID;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postTime;

    public Message() {
        // 反序列化用
    }

    public Message(int id, String content, int userID, int receiverID, LocalDateTime postTime) {
        this.id = id;
        this.content = content;
        this.userID = userID;
        this.receiverID = receiverID;
        this.postTime = postTime;
    }

    public Message(String content, int userID, int receiverID, LocalDateTime postTime) { // 新增資料用的 無id
        this.content = content;
        this.userID = userID;
        this.receiverID = receiverID;
        this.postTime = postTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public void setPostTime(LocalDateTime postTime) {
        this.postTime = postTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", userID=" + userID +
                ", receiverID=" + receiverID +
                ", postTime=" + postTime +
                '}';
    }
}
