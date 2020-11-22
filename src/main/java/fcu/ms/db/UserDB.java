package fcu.ms.db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import fcu.ms.data.User;
import fcu.ms.data.UserBuilder;
import fcu.ms.dbUtil.MySqlBoneCP;

public class UserDB {

    private static final UserDB userDB = new UserDB();

    public static UserDB getInstance() {
        return userDB;
    }

    private UserDB(){

    }


    public List<User> getUsers() {

        Connection connection = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;

        List<User> users = new ArrayList<User>();

        String sqlString = "SELECT * FROM `user`";
        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);
            rs = preStmt.executeQuery();

            while (rs.next()) {
                User user = parseUserFromDbColumn(rs);
                users.add(user);
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

        return users;
    }

    public User getUser(int id) {
        User user = null;
        String sqlString = "SELECT * FROM `user` WHERE `id` = ?";

        Connection connection = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setInt(1, id);
            rs = preStmt.executeQuery();

            while (rs.next()) {
                user = parseUserFromDbColumn(rs);
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

        return user;
    }

    public User getUserByFirebaseUID(String uid) {
        User user = null;

        String sqlString = "SELECT * FROM `user` WHERE `firebase_uid` = ? ";

        Connection connection = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setString(1, uid);
            rs = preStmt.executeQuery();
            while ( rs.next() ) {
                user = parseUserFromDbColumn(rs);
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

        return user;
    }

    public boolean changeUserPint(int point, int userId) {
        boolean is_success = false;

        String sqlString = "UPDATE `user` " +
                           "SET `user`.`point` = ? " +
                           "WHERE `user`.`id` = ? ";

        Connection connection = null;
        PreparedStatement preStmt = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setInt(1, point);
            preStmt.setInt(2, userId);
            preStmt.executeUpdate();
            is_success = true;

        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }

        try {
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return is_success;
    }

    public boolean createUser(User user) {

        boolean is_success = false;
        String sqlString = "INSERT INTO `user` (`name`, `firebase_uid`) VALUES (?, ?)";

        Connection connection = null;
        PreparedStatement preStmt = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setString(1, user.getName());
            preStmt.setString(2, user.getFirebaseUid());
            preStmt.executeUpdate();

            is_success = true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }

        try {
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return is_success;
    }

    public boolean deleteUser(String name) {

        boolean is_success = false;
        String sqlString = "DELETE FROM `user` WHERE `name` = ?";

        Connection connection = null;
        PreparedStatement preStmt = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setString(1, name);
            preStmt.executeUpdate();

            is_success = true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }

        try {
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return is_success;
    }

    public boolean deleteUser(int id) {

        boolean is_success = false;
        String sqlString = "DELETE FROM `user` WHERE `id` = ?";

        Connection connection = null;
        PreparedStatement preStmt = null;

        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setInt(1, id);
            preStmt.executeUpdate();

            is_success = true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }

        try {
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return is_success;
    }

    private User parseUserFromDbColumn(ResultSet dbResult) throws Exception {
        int id = dbResult.getInt("id");
        String name = dbResult.getString("name");
        String firebase_uid = dbResult.getString("firebase_uid");
        int point = dbResult.getInt("point");

        return UserBuilder.anUser(id)
                .withName(name)
                .withFirebaseUid(firebase_uid)
                .withPoint(point)
                .build();
    }
}
