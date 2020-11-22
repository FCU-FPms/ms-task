package fcu.ms.provider;

import fcu.ms.data.Message;
import fcu.ms.data.Task;
import fcu.ms.data.User;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilForJson {

    public static Map<String, JSONObject> getEachUser(List<User> users) {
        Map<String, JSONObject> entities = new HashMap<>();

        for (User user : users) {
            int userId = user.getId();
            JSONObject entity = getUserEntity(user);
            entities.put(String.valueOf(userId), entity);
        }
        return entities;
    }

    public static JSONObject getUserEntity(User user) {
        JSONObject entity = new JSONObject();
        entity.put("name", user.getName());
        entity.put("firebase_uid", user.getFirebaseUid());
        return entity;
    }

    public static List<JSONObject> getEachMessage(List<Message> messages) {

        List<JSONObject> entities = new ArrayList<>();

        for (Message message : messages) {
            JSONObject entity = getMessageEntity(message);
            entities.add(entity);
        }
        return entities;
    }

    public static JSONObject getMessageEntity(Message message) {
        JSONObject entity = new JSONObject();
        entity.put("content", message.getContent());
        entity.put("userID", message.getUserID());
        entity.put("receiverID", message.getReceiverID());
        entity.put("postTime", message.getPostTime());
        return entity;
    }


    public static List<JSONObject> getEachTask(List<Task> tasks) {

        List<JSONObject> entities = new ArrayList<>();

        for (Task task : tasks) {
            JSONObject entity = getTaskEntity(task);
            entities.add(entity);
        }
        return entities;
    }

    public static JSONObject getTaskEntity(Task task) {
        JSONObject entity = new JSONObject();
        entity.put("TaskID", task.getTaskID());
        entity.put("TaskName", task.getName());
        entity.put("Message", task.getMessage());
        entity.put("StartPostTime", task.getStartPostTime());
        entity.put("EndPostTime", task.getEndPostTime());
        entity.put("Salary", task.getSalary());
        entity.put("ReleaseUserID", task.getReleaseUserID());
        entity.put("ReleaseTime", task.getReleaseTime());
        entity.put("ReceiveUserID", task.getReceiveUserID());
        entity.put("TaskAddress", task.getTaskAddress());
        return entity;
    }

}
