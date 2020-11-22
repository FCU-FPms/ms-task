package fcu.ms.db;

import fcu.ms.data.Task;
import fcu.ms.data.TaskBuilder;
import fcu.ms.dbUtil.MySqlBoneCP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;

public class TaskDB {
    private static final TaskDB taskDB = new TaskDB();

    public static TaskDB getInstance() {
        return taskDB;
    }

    private TaskDB() {

    }

    public boolean createTask(Task task) {
        String sqlString = "INSERT INTO `task`" +
                "(`name`," +
                " `message`," +
                " `start_post_time`," +
                " `end_post_time`," +
                " `salary`," +
                " `release_user_id`," +
                " `release_time`," +
                " `receive_user_id`," +
                " `task_address`)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        boolean is_success;

        Connection connection = null;
        PreparedStatement preStmt = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);

            preStmt.setString(1, task.getName());
            preStmt.setString(2, task.getMessage());
            preStmt.setTimestamp(3, Util.transitLocalDateTime(task.getStartPostTime()));
            preStmt.setTimestamp(4, Util.transitLocalDateTime(task.getEndPostTime()));
            preStmt.setInt(5, task.getSalary());
            preStmt.setInt(6,task.getReleaseUserID());
            preStmt.setTimestamp(7, Util.transitLocalDateTime(task.getReleaseTime()));
            preStmt.setInt(8,task.getReceiveUserID());
            preStmt.setString(9, task.getTaskAddress());


            preStmt.executeUpdate();

            is_success = true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
            is_success = false;
        }

        try {
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return is_success;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        String sqlString = "SELECT * FROM `task` ORDER BY `release_time` DESC";
        try {
            Connection connection = MySqlBoneCP.getInstance().getConnection();
            PreparedStatement preStmt = connection.prepareStatement(sqlString);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                Task task = parseTaskFromDbColumn(rs);
                tasks.add(task);
            }
            rs.close();
            preStmt.close();
            connection.close();

        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
        return tasks;
    }

    public List<Task> getTasksWithoutMyTask(int userId) {
        List<Task> tasks = new ArrayList<>();

        String sqlString = "SELECT * FROM `task` WHERE `release_user_id` != ? ORDER BY `release_time` DESC";
        try {
            Connection connection = MySqlBoneCP.getInstance().getConnection();
            PreparedStatement preStmt = connection.prepareStatement(sqlString);
            preStmt.setInt(1, userId);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                Task task = parseTaskFromDbColumn(rs);
                tasks.add(task);
            }
            rs.close();
            preStmt.close();
            connection.close();

        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
        return tasks;
    }

    public List<Task> getUserReleaseTasks(int userId) {
        List<Task> tasks = new ArrayList<>();

        String sqlString = "SELECT * FROM `task` WHERE `release_user_id` = ? ORDER BY `release_time` DESC ";
        try {
            Connection connection = MySqlBoneCP.getInstance().getConnection();
            PreparedStatement preStmt = connection.prepareStatement(sqlString);
            preStmt.setInt(1, userId);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                Task task = parseTaskFromDbColumn(rs);
                tasks.add(task);
            }
            rs.close();
            preStmt.close();
            connection.close();

        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
        return tasks;
    }

    // Todo 之後需要照申請時間來排序
    // 有排除進行中的
    public List<Task> getUserRequestTasks(int userId) {
        List<Task> tasks = new ArrayList<>();

        String sqlString = "SELECT `task`.* FROM `task`,`request_task_users` " +
                           "WHERE `request_task_users`.`task_id` = `task`.`id` " +
                           "AND `request_task_users`.`user_id` = ?" +
                           "AND `task`.`receive_user_id` != ?";
        try {
            Connection connection = MySqlBoneCP.getInstance().getConnection();
            PreparedStatement preStmt = connection.prepareStatement(sqlString);
            preStmt.setInt(1, userId);
            preStmt.setInt(2, userId);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                Task task = parseTaskFromDbColumn(rs);
                tasks.add(task);
            }
            rs.close();
            preStmt.close();
            connection.close();

        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
        return tasks;
    }


    public List<Task> getUserReceiveAndOnGoingTasks(int userId) {
        List<Task> tasks = new ArrayList<>();

        String sqlString = "SELECT `task`.* FROM `task`, `save_tasks_state` WHERE `task`.`receive_user_id` = ? " +
                           "AND `save_tasks_state`.`taskID` = `task`.`id`" +
                           "AND `save_tasks_state`.`step_time` = " +
                                "( SELECT  MAX( `step_time` )" +
                                "FROM `task`, `save_tasks_state`" +
                                "WHERE `save_tasks_state`.`taskID` = `task`.`id`)" +
                           "AND (`save_tasks_state`.`task_state_id` != 6 AND `save_tasks_state`.`task_state_id` != 7)";
        try {
            Connection connection = MySqlBoneCP.getInstance().getConnection();
            PreparedStatement preStmt = connection.prepareStatement(sqlString);
            preStmt.setInt(1, userId);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                Task task = parseTaskFromDbColumn(rs);
                tasks.add(task);
            }
            rs.close();
            preStmt.close();
            connection.close();

        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
        return tasks;
    }

    public List<Task> getUserEndTasks(int userId) { // 拿已經結束的任務 不管是不是完美完成 或是 失敗的完成
        List<Task> tasks = new ArrayList<>();

        String sqlString = "SELECT * FROM `task`, `save_tasks_state` " +
                "WHERE `task`.`receive_user_id` = ? " +
                "AND `save_tasks_state`.`taskID` = `task`.`id` " +
                "AND (`save_tasks_state`.`task_state_id` = 6 OR `save_tasks_state`.`task_state_id` = 7)";
        // 6 7 是結束狀態的值


        try {
            Connection connection = MySqlBoneCP.getInstance().getConnection();
            PreparedStatement preStmt = connection.prepareStatement(sqlString);
            preStmt.setInt(1, userId);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                Task task = parseTaskFromDbColumn(rs);
                tasks.add(task);
            }
            rs.close();
            preStmt.close();
            connection.close();

        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
        return tasks;
    }


    public int getTaskIdByName(String taskName) {
        int id = -1;
        String sqlString = "SELECT `id` FROM `task` WHERE `name` = ?";

        Connection connection = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setString(1, taskName);
            rs = preStmt.executeQuery();

            while (rs.next()) {
                id = rs.getInt("id");
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }

        try {
            rs.close();
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }


    public Task getTask(int taskId) {
        Task task = null;
        String sqlString = "SELECT * FROM `task` WHERE `id` = ?";

        Connection connection = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setInt(1, taskId);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                task = parseTaskFromDbColumn(rs);
            }


        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }

        try {
            rs.close();
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return task;

    }

    public boolean setTask(Task task){
        boolean is_success = false;
        try {
            int taskId = task.getTaskID();
            setTaskName(task.getName(), taskId);
            setTaskMessage(task.getMessage(), taskId);
            setTaskStartPostTime(task.getStartPostTime(), taskId);
            setTaskEndPostTime(task.getStartPostTime(), taskId);
            // 無法設定 salary
            // 無法設定 releaseUserID
            // 無法設定 releaseTime
            setTaskReceiveUserID(task.getReceiveUserID(), taskId);
            setTaskAddress(task.getTaskAddress(), taskId);

            is_success = true;
        }catch (Exception ex){
            System.out.println("Error: " + ex);
            is_success = false;
        }
        return is_success;
    }

    private void setTaskName(String taskName, int taskID) {
        if(taskName == null) {
            return;
        }

        Connection connection = null;
        PreparedStatement preStmt = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            String sqlString = "UPDATE `task` SET `name` = ? WHERE `id` = ?";

            preStmt = connection.prepareStatement(sqlString);
            preStmt.setString(1, taskName);
            preStmt.setInt(2, taskID);
            preStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTaskMessage(String taskMessage, int taskID) {
        if(taskMessage == null) {
            return;
        }

        Connection connection = null;
        PreparedStatement preStmt = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            String sqlString = "UPDATE `task` SET `message` = ? WHERE `id` = ?";
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setString(1, taskMessage);
            preStmt.setInt(2, taskID);

            preStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setTaskStartPostTime(LocalDateTime taskStartPostTime, int taskID) {
        if(taskStartPostTime == null) {
            return;
        }

        Connection connection = null;
        PreparedStatement preStmt = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            String sqlString = "UPDATE `task` SET `start_post_time` = ? WHERE `id` = ?";
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setTimestamp(1, Util.transitLocalDateTime(taskStartPostTime));
            preStmt.setInt(2, taskID);

            preStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTaskEndPostTime(LocalDateTime taskEndPostTime, int taskID) {
        if(taskEndPostTime == null) {
            return;
        }

        Connection connection = null;
        PreparedStatement preStmt = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            String sqlString = "UPDATE `task` SET `end_post_time` = ? WHERE `id` = ?";
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setTimestamp(1, Util.transitLocalDateTime(taskEndPostTime));
            preStmt.setInt(2, taskID);

            preStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 目前沒有修改價錢的函式

    // 無法修改 release_user_id

    // 無法修改 release_time

    private void setTaskReceiveUserID(int receiveUserID, int taskID) {

        Connection connection = null;
        PreparedStatement preStmt = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            String sqlString = "UPDATE `task` SET `receive_user_id` = ? WHERE `id` = ?";
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setInt(1, receiveUserID);
            preStmt.setInt(2, taskID);

            preStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setTaskAddress(String taskAddress, int taskID) {
        if(taskAddress == null) {
            return;
        }

        Connection connection = null;
        PreparedStatement preStmt = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            String sqlString = "UPDATE `task` SET `task_address` = ? WHERE `id` = ?";
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setString(1, taskAddress);
            preStmt.setInt(2, taskID);

            preStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean deleteTask(int taskID) {

        Connection connection = null;
        PreparedStatement preStmt = null;

        boolean is_success;
        String sqlString = "DELETE FROM `task` WHERE `id` = ?";
        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setInt(1, taskID);

            preStmt.executeUpdate();

            is_success = true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
            is_success = false;
        }

        try {
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is_success;
    }

    private Task parseTaskFromDbColumn(ResultSet dbResult) throws Exception {

        // 10 cols
        int id = dbResult.getInt("id");
        String taskName = dbResult.getString("name");
        String message = dbResult.getString("message");
        LocalDateTime startPostTime = Util.transitTimestamp(dbResult.getTimestamp("start_post_time"));
        LocalDateTime endPostTime = Util.transitTimestamp(dbResult.getTimestamp("end_post_time"));

        int salary = dbResult.getInt("salary");
        int releaseUserId = dbResult.getInt("release_user_id");
        LocalDateTime releaseTime = Util.transitTimestamp(dbResult.getTimestamp("release_time"));
        int receiveUserId = dbResult.getInt("receive_user_id");
        String taskAddress = dbResult.getString("task_address");

        return TaskBuilder.aTask(id, salary, releaseUserId, releaseTime)
                .withName(taskName)
                .withMessage(message)
                .withStartPostTime(startPostTime)
                .withEndPostTime(endPostTime)
                .withReceiveUserID(receiveUserId)
                .withTaskAddress(taskAddress).build();
    }

}
