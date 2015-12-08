package com.example.anna.octopuschat.dbTables;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by anna on 06/12/15.
 */

@Table(name = "messages")
public class Message extends Model {
    @Column(name = "messageId")
    public int messageId;

    @Column(name = "contactId")
    public int contactId;

    @Column(name = "message")
    public String message;

    @Column(name = "dispatchTimestamp")
    public long dispatchTimestamp;

    @Column(name = "inbox")
    public boolean inbox;

    public Message() {
        super();
    }

    public Message(int messageId, int contactId, String message, long dispatchTimestamp, boolean inbox) {
        super();
        this.messageId = messageId;
        this.contactId = contactId;
        this.message = message;
        this.dispatchTimestamp = dispatchTimestamp;
        this.inbox = inbox;
    }

    public static List<Message> getMessages(int contactId) {
        return new Select()
                .from(Message.class)
                .where("contactId = ?", contactId)
                .execute();
    }

    public static Message getMessage(int messageId) {
        return new Select()
                .from(Message.class)
                .where("messageId = ?", messageId)
                .executeSingle();
    }

    public static Message getMessage(JSONObject json) {
        try {
            int id = json.getInt("id");
            int contactId = json.getInt("recipientId");
            boolean inbox = false;
            if (contactId == Profile.getProfile().userId) {
                contactId = json.getInt("senderId");
                inbox = true;
            }
            String message = json.getString("message");
            int dispatchTimestamp = json.getInt("dispatchTimestamp");
            return new Message(id, contactId, message, dispatchTimestamp, inbox);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
