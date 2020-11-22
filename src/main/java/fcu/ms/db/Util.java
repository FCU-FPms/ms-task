package fcu.ms.db;

import fcu.ms.data.User;
import fcu.ms.data.UserBuilder;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Util {

    public static Timestamp transitLocalDateTime(LocalDateTime localDateTime) { // 如果是null 會回傳null
        if(localDateTime != null) {
            return Timestamp.valueOf(localDateTime);
        } else {
            return null;
        }
    }

    public static LocalDateTime transitTimestamp(Timestamp timestamp) { // 如果是null 會回傳null
        if(timestamp != null) {
            return timestamp.toLocalDateTime();
        } else {
            return null;
        }
    }

    public static User parseUserFromDbColumn(ResultSet dbResult) throws Exception {
        int id = dbResult.getInt("id");
        String name = dbResult.getString("name");
        String firebase_uid = dbResult.getString("firebase_uid");
        return UserBuilder.anUser(id)
                .withName(name)
                .withFirebaseUid(firebase_uid).build();
    }

}
