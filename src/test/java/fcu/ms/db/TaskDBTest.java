package fcu.ms.db;

import fcu.ms.data.Task;
import fcu.ms.data.TaskBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskDBTest {

    static final TaskDB taskDB = TaskDB.getInstance();
    static final String taskname = "中文";

    @Test
    public void createTask() {
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);

        Task task = TaskBuilder.aTask(0, 16888, 1, currentTime)
                .withName(taskname)
                .build();
        assertTrue( taskDB.createTask(task) );
    }


    @Test
    void getTasks() {
        List<Task> taskList = taskDB.getTasks();
        assertTrue(taskList.size() > 0); // 如果沒資料會報錯
    }


    @Test
    void getTaskIdByName() {
        assertTrue(taskDB.getTaskIdByName(taskname) > 0);
    }


    @Test
    void getTask() {
        int id = taskDB.getTaskIdByName(taskname);
        Task task =taskDB.getTask(id);
        assertEquals(taskname, task.getName());
    }

    @Test
    public void setTask(){
        int id = taskDB.getTaskIdByName(taskname);

        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);

        Task task = TaskBuilder.aTask(id, 888, 1, currentTime)
                .withMessage("testfranky")
                .build();

        assertTrue(taskDB.setTask(task));
    }


    @Test
    void deleteTask() {
        assertTrue(taskDB.deleteTask(458));
    }

    @Test
    void getUserRequestTasks() {
        List<Task> taskList = taskDB.getUserRequestTasks(21);
        assertTrue(taskList.size() > 0); // 如果沒資料會報錯
    }

    @Test
    void getReceiveUserTasks() {
        List<Task> taskList = taskDB.getUserReceiveAndOnGoingTasks(21);
        assertTrue(taskList.size() > 0); // 如果沒資料會報錯
    }

    @Test
    void getUserEndTasks() {
        List<Task> taskList = taskDB.getUserEndTasks(14);
        assertTrue(taskList.size() > 0); // 如果沒資料會報錯
    }
}
