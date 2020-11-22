package fcu.ms.db;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fcu.ms.data.Message;
import fcu.ms.data.Task;
import fcu.ms.data.User;
import fcu.ms.dbUtil.MySqlBoneCP;


public class MessageDB {
    private static final MessageDB messageDB = new MessageDB();

    public static MessageDB getInstance() {
        return messageDB;
    }

    private MessageDB(){

    }

    public List<Message> getMessageByTwoUserID(int user1ID, int user2ID) {
        List<Message> messages = new ArrayList<Message>();

        Connection connection = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;


        String sqlString = "SELECT * FROM `message` " +
                "WHERE (`userID` = ? OR `receiverID` = ?  ) " +
                "AND ( `userID`= ? OR `receiverID`= ?) " +
                "ORDER BY `postTime` ASC;";
        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setInt(1, user1ID);
            preStmt.setInt(2, user1ID);

            preStmt.setInt(3, user2ID);
            preStmt.setInt(4, user2ID);

            rs = preStmt.executeQuery();
            while (rs.next()) {
                Message message = parseMessageFromDbColumn(rs);
                messages.add(message);
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
        return messages;
    }
    public List<Message> getLatestMessageByTwoUserID(int user1ID, int user2ID) {
        List<Message> messages = new ArrayList<Message>();

        Connection connection = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;


        String sqlString = "SELECT * FROM `message` WHERE postTime IN" +
                "(SELECT MAX(postTime)" +
                "FROM `message`" +
                "WHERE (`userID` = ? OR `receiverID` = ?) AND (`userID`= ? OR `receiverID`= ?));";
        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setInt(1, user1ID);
            preStmt.setInt(2, user1ID);

            preStmt.setInt(3, user2ID);
            preStmt.setInt(4, user2ID);

            rs = preStmt.executeQuery();
            while (rs.next()) {
                Message message = parseMessageFromDbColumn(rs);
                messages.add(message);
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
        return messages;
    }

    public List<User> getUserRelatedWho(int userID) {
        List<User> users = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;

        String sqlString = "SELECT * FROM `user` " +
                "WHERE `id` IN" +
                " (SELECT `userID` FROM `message` WHERE `receiverID` = ? " +
                " UNION " +
                " SELECT `receiverID` FROM `message` WHERE `userID` = ?)";
        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);

            preStmt.setInt(1, userID);
            preStmt.setInt(2, userID);

            rs = preStmt.executeQuery();
            while (rs.next()) {
                User user = Util.parseUserFromDbColumn(rs);
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

    public boolean createMessage(Message message) {
        boolean is_success = false;

        Connection connection = null;
        PreparedStatement preStmt = null;

        String sqlString = "INSERT INTO `message` (content, userID, receiverID, postTime) VALUES( ?, ?, ?, ?)";
        try {
            connection = MySqlBoneCP.getInstance().getConnection();
            preStmt = connection.prepareStatement(sqlString);
            preStmt.setString(1, message.getContent());
            preStmt.setInt(2, message.getUserID());
            preStmt.setInt(3, message.getReceiverID());
            preStmt.setTimestamp(4, Timestamp.valueOf(message.getPostTime()));
            preStmt.executeUpdate();

            is_success = true;
        } catch (Exception ex) {
            System.out.println("Error: "+ex);
        }

        try {
            preStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return is_success;
    }

    private Message parseMessageFromDbColumn(ResultSet dbResult) throws Exception {
        int id = dbResult.getInt("id");
        String content = dbResult.getString("content");
        int userID = dbResult.getInt("userID");
        int receiverID = dbResult.getInt("receiverID");
        LocalDateTime postTime = dbResult.getTimestamp("postTime").toLocalDateTime();
        return new Message(id, content, userID, receiverID, postTime);
    }
}
