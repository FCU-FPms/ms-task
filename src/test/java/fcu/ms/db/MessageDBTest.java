package fcu.ms.db;

import fcu.ms.data.Message;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class MessageDBTest {

    @Test
    public void createMessage_TEST() {   /* 創建一個新的 message */
        MessageDB messageDB = MessageDB.getInstance();
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        Message message = new Message("4", 1, 14, currentTime);
        assertTrue( messageDB.createMessage(message) );
    }

    @Test
    void getUserRelatedWho_TEST() {
        MessageDB messageDB = MessageDB.getInstance();
        System.out.println(messageDB.getUserRelatedWho(14));
    }
    @Test
    void getLatestMessage_TEST() {
        MessageDB messageDB = MessageDB.getInstance();
        System.out.println(messageDB.getLatestMessageByTwoUserID(14,21));
    }

    @Test
    void getMessageByTwoUserID_TEST() {
        MessageDB messageDB = MessageDB.getInstance();
        System.out.println(messageDB.getMessageByTwoUserID(14, 1));
    }
}