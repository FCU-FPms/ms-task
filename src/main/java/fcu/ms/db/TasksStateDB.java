package fcu.ms.db;

import fcu.ms.data.TaskState;
import fcu.ms.data.TaskStateEnum;
import fcu.ms.dbUtil.MySqlBoneCP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

public class TasksStateDB {
    private static final TasksStateDB tasksStateDB = new TasksStateDB();

    public static TasksStateDB getTasksStateDB() {
        return tasksStateDB;
    }

    private TasksStateDB() {

    }

    public boolean createTaskState(TaskState taskState) {
        String sqlString = "INSERT INTO `save_tasks_state`" +
                "(`taskID`," +
                " `task_state_id`," +
                " `step_time`) VALUES (?, ?, ?)";

        boolean is_success;

        Connection connection = null;
        PreparedStatement preStmt = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);

            preStmt.setInt(1, taskState.getTaskID());
            preStmt.setInt(2, taskState.getTaskState().getValue());
            preStmt.setTimestamp(3, Util.transitLocalDateTime(taskState.getStepTime()));

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

    public TaskState getLatestStateByTaskID(int taskID) {
        String sqlString = "SELECT * FROM `save_tasks_state` " +
                "WHERE `step_time` = ( SELECT  MAX( `step_time` ) " +
                "FROM `save_tasks_state` WHERE `taskID` = ? )";

        TaskState taskState = null;

        Connection connection = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);

            preStmt.setInt(1, taskID);

            rs = preStmt.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("id");
                int taskId = rs.getInt("taskID");
                int taskStateId = rs.getInt("task_state_id");
                LocalDateTime step_time = Util.transitTimestamp(rs.getTimestamp("step_time"));

                taskState = new TaskState(id, taskId, TaskStateEnum.valueOf(taskStateId), step_time);
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
        return taskState;
    }
}
