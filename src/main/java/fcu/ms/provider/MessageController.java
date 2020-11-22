package fcu.ms.provider;
import fcu.ms.data.Message;
import fcu.ms.data.User;
import fcu.ms.db.MessageDB;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value ="/message")
public class MessageController {
    MessageDB messageDB = MessageDB.getInstance();


    @PostMapping(value = "")
    public ResponseEntity<String> createMessage(@RequestBody Message message) {

        if(message.getPostTime() == null) { // 會自動填入傳送訊息時的時間點
            LocalDateTime postTime = LocalDateTime.now(ZoneOffset.UTC);
            message.setPostTime(postTime);
        }

        boolean is_success = messageDB.createMessage(message);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        if(is_success) {
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Error to build Message in DB", headers, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/userRelatedWho/{userID}")
    public ResponseEntity<Object> getUserRelatedWho(@PathVariable int userID) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        List<User> users = messageDB.getUserRelatedWho(userID);
        Map<String, JSONObject> entities = UtilForJson.getEachUser(users);
        return new ResponseEntity<Object>(entities, headers, HttpStatus.OK);
    }
    @GetMapping(value = "/getLatestMessage/{user1ID}/{user2ID}")
    public ResponseEntity<Object> getLatestMessage(@PathVariable int user1ID, @PathVariable int user2ID) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        List<Message> messages = messageDB.getLatestMessageByTwoUserID(user1ID, user2ID);
        List<JSONObject> entities = UtilForJson.getEachMessage(messages);

        return new ResponseEntity<Object>(entities, headers, HttpStatus.OK);
    }


    @GetMapping(value = "/allChatMessage/{user1ID}/{user2ID}")
    public ResponseEntity<Object> getUserRelatedWho(@PathVariable int user1ID, @PathVariable int user2ID) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        List<Message> messages = messageDB.getMessageByTwoUserID(user1ID, user2ID);

        List<JSONObject> entities = UtilForJson.getEachMessage(messages);
        return new ResponseEntity<Object>(entities, headers, HttpStatus.OK);
    }
}
