package fcu.ms.db;

import fcu.ms.data.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RequestTaskUsersDBTest {

    static final RequestTaskUsersDB instance  = RequestTaskUsersDB.getInstance();

    @Test
    public void addUserToTaskRequestList() {
        int userID = 1;
        int TaskID = 325;
        assertTrue( instance.addUserToTaskRequestList(userID, TaskID) );
    }

    @Test
    public void removeUserFromTaskRequestList() {
        int userID = 1;
        int TaskID = 325;
        assertTrue( instance.removeUserFromTaskRequestList(userID, TaskID) );
    }

    @Test
    public void getRequestUsersID() {
        int TaskID = 461;
        List<User> users = instance.getRequestUsers(TaskID);
        System.out.println(users);
    }

    @Test
    void isUserAlreadyInList() {

        int userID = 21;
        int TaskID = 420;
        assertTrue( instance.isUserAlreadyInList(userID, TaskID) );
        assertFalse( instance.isUserAlreadyInList(userID+1, TaskID) );

    }
}