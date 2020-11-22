package fcu.ms.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.fge.jsonpatch.JsonPatchException;

import fcu.ms.data.Task;
import fcu.ms.data.TaskState;
import fcu.ms.data.User;
import fcu.ms.db.RequestTaskUsersDB;
import fcu.ms.db.TasksStateDB;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fcu.ms.db.TaskDB;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.fge.jsonpatch.JsonPatch;

@RestController
@RequestMapping(value ="/tasks")
public class TaskController {
    TaskDB taskDB = TaskDB.getInstance();
    TasksStateDB tasksStateDB = TasksStateDB.getTasksStateDB();
    RequestTaskUsersDB requestTaskUsersDB = RequestTaskUsersDB.getInstance();

    @PostMapping(value = "")
    public ResponseEntity<String> createTask(@RequestBody Task task) {
        // postTime 在API中要打上 yyyy-mm-dd hh:mm:ss 格式

        if(task.getReleaseTime() == null) {
            LocalDateTime releaseTime = LocalDateTime.now(ZoneOffset.UTC); // 會自動填入發布時的時間點
            task.setReleaseTime(releaseTime);
        }

        boolean is_success = taskDB.createTask(task);

        HttpHeaders headers = createBaseHttpHeaders();

        if(is_success) {
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Error to build Task in DB", headers, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("")
    public ResponseEntity<Object> getTasks() {
        HttpHeaders headers = createBaseHttpHeaders();

        List<Task> taskList = taskDB.getTasks();
        List<JSONObject> entities = UtilForJson.getEachTask(taskList);

        return new ResponseEntity<Object>(entities, headers, HttpStatus.OK);
    }

    @GetMapping("/WithoutUserId/{userId}")
    public ResponseEntity<Object> getTasksWithoutMyTask(@PathVariable int userId) {
        HttpHeaders headers = createBaseHttpHeaders();

        List<Task> taskList = taskDB.getTasksWithoutMyTask(userId);
        List<JSONObject> entities = UtilForJson.getEachTask(taskList);
        return new ResponseEntity<Object>(entities, headers, HttpStatus.OK);
    }

    @GetMapping("/UserRequestTasks/{userId}")
    public ResponseEntity<Object> getUserRequestTasks(@PathVariable int userId) {
        HttpHeaders headers = createBaseHttpHeaders();

        List<Task> taskList = taskDB.getUserRequestTasks(userId);
        List<JSONObject> entities = UtilForJson.getEachTask(taskList);
        return new ResponseEntity<Object>(entities, headers, HttpStatus.OK);
    }

    @GetMapping("/UserReceiveTasks/{userId}") // 就是在進行中的
    public ResponseEntity<Object> getReceiveUserTasks(@PathVariable int userId) {
        HttpHeaders headers = createBaseHttpHeaders();

        List<Task> taskList = taskDB.getUserReceiveAndOnGoingTasks(userId);
        List<JSONObject> entities = UtilForJson.getEachTask(taskList);

        return new ResponseEntity<Object>(entities, headers, HttpStatus.OK);
    }

    @GetMapping("/UserEndTasks/{userId}") // 就是已結束的
    public ResponseEntity<Object> getUserEndTasks(@PathVariable int userId) {
        HttpHeaders headers = createBaseHttpHeaders();

        List<Task> taskList = taskDB.getUserEndTasks(userId);
        List<JSONObject> entities = UtilForJson.getEachTask(taskList);
        return new ResponseEntity<Object>(entities, headers, HttpStatus.OK);
    }

    @GetMapping("/ReleaseUser/{userId}")
    public ResponseEntity<Object> getUserReleaseTasks(@PathVariable int userId) {
        HttpHeaders headers = createBaseHttpHeaders();

        List<Task> taskList = taskDB.getUserReleaseTasks(userId);
        List<JSONObject> entities = UtilForJson.getEachTask(taskList);
        return new ResponseEntity<Object>(entities, headers, HttpStatus.OK);
    }


    @GetMapping("/{TaskID}")
    public ResponseEntity<Object> getTaskByID(@PathVariable int TaskID) {
        HttpHeaders headers = createBaseHttpHeaders();

        Task task = taskDB.getTask(TaskID);

        Map<String, JSONObject> entities = new HashMap<String, JSONObject>();
        if(task != null) {
            JSONObject entity = getTaskEntity(task);
            int taskId = task.getTaskID();
            entities.put(String.valueOf(taskId), entity);
            return new ResponseEntity<Object>(entities, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(headers, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping (path = "/{taskId}", consumes = "application/json-patch+json")
    public ResponseEntity<String> updateTask(@PathVariable int taskId, @RequestBody JsonPatch patch) {
        HttpHeaders headers = createBaseHttpHeaders();
        boolean is_success = false;

        try {
            Task task = taskDB.getTask(taskId);
            Task taskPatched = applyPatchToTask(patch, task);
            is_success = taskDB.setTask(taskPatched);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(is_success) {
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Error to SET Task in DB", headers, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{TaskID}")
    public ResponseEntity<String> deleteTaskByID(@PathVariable int TaskID){
        boolean is_success = taskDB.deleteTask(TaskID);

        HttpHeaders headers = createBaseHttpHeaders();

        if(is_success){
            return new ResponseEntity<String>(headers, HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<String>("Error to delete Task in DB", headers, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/{taskID}/state")
    public ResponseEntity<String> addTaskState(@PathVariable int taskID, @RequestBody TaskState taskState) {
        // postTime 在API中要打上 yyyy-mm-dd hh:mm:ss 格式

        taskState.setTaskID(taskID);
        if(taskState.getStepTime() == null) {
            LocalDateTime releaseTime = LocalDateTime.now(ZoneOffset.UTC); // 會自動填入發布時的時間點
            taskState.setStepTime(releaseTime);
        }

        boolean is_success = tasksStateDB.createTaskState(taskState);

        HttpHeaders headers = createBaseHttpHeaders();

        if(is_success) {
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Error to build addTaskState in DB", headers, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{taskID}/state")
    public ResponseEntity<Object> getLatestTaskState(@PathVariable int taskID) {
        HttpHeaders headers = createBaseHttpHeaders();

        TaskState taskState = tasksStateDB.getLatestStateByTaskID(taskID);

        Map<String, JSONObject> entities = new HashMap<String, JSONObject>();
        if(taskState != null) {
            JSONObject entity = new JSONObject();
            entity.put("taskState", taskState.getTaskState());
            entity.put("stepTime", taskState.getStepTime());

            return new ResponseEntity<Object>(entity, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(headers, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping(value = "/{taskID}/RequestUsers/{userID}")
    public ResponseEntity<String> addUserToTaskRequestList(@PathVariable int taskID, @PathVariable int userID) {
        if ( requestTaskUsersDB.isUserAlreadyInList(userID, taskID) ) {
            return sendCreateResponse(true);
        } else {
            boolean is_success = requestTaskUsersDB.addUserToTaskRequestList(userID, taskID);
            return sendCreateResponse(is_success);
        }
    }

    @DeleteMapping(value = "/{taskID}/RequestUsers/{userID}")
    public ResponseEntity<String> removeUserFromTaskRequestList(@PathVariable int taskID, @PathVariable int userID) {
        // postTime 在API中要打上 yyyy-mm-dd hh:mm:ss 格式

        boolean is_success = requestTaskUsersDB.removeUserFromTaskRequestList(userID, taskID);

        HttpHeaders headers = createBaseHttpHeaders();

        if(is_success) {
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Error to removeUserFromTaskRequestList in DB", headers, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{taskID}/RequestUsers")
    public ResponseEntity<Object> getTaskRequestList(@PathVariable int taskID) {
        HttpHeaders headers = createBaseHttpHeaders();

        List<User> users = requestTaskUsersDB.getRequestUsers(taskID); // getRequestUsers

        List<JSONObject> entities = new ArrayList<JSONObject>();

        for (User user : users) {
            JSONObject entity = new JSONObject();
            entity.put("id", user.getId());
            entity.put("name", user.getName());
            entity.put("firebase_uid", user.getFirebaseUid());
            entities.add(entity);
        }

        return new ResponseEntity<Object>(entities, headers, HttpStatus.OK);
    }

    @GetMapping("/{taskID}/checkUserAlreadyRequest/{userID}")
    public ResponseEntity<Object> checkUserAlreadyRequest(@PathVariable int taskID, @PathVariable int userID) {
        HttpHeaders headers = createBaseHttpHeaders();
        boolean isUserAlreadyRequest = requestTaskUsersDB.isUserAlreadyInList(userID, taskID); // getRequestUsers
        return new ResponseEntity<Object>(isUserAlreadyRequest, headers, HttpStatus.OK);
    }


    private JSONObject getTaskEntity(Task task) {
        JSONObject entity = new JSONObject();
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

    private Task applyPatchToTask(JsonPatch patch, Task targetTask) throws JsonPatchException, JsonProcessingException {
        // 參考 https://github.com/FasterXML/jackson-modules-java8
        // 為了支持java8的新型別, 像是localTime
        ObjectMapper objectMapper = new ObjectMapper() // Todo 這裡應該用靜態的 還需要改
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule()); // new module, NOT JSR310Module


        JsonNode patched = patch.apply(objectMapper.convertValue(targetTask, JsonNode.class)); //這可以自動轉換task
        return objectMapper.treeToValue(patched, Task.class);
    }

    private HttpHeaders createBaseHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return headers;
    }

    private ResponseEntity<String> sendCreateResponse(boolean is_success) {
        HttpHeaders headers = createBaseHttpHeaders();
        if(is_success) {
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Error in create", headers, HttpStatus.BAD_REQUEST);
        }
    }

}
