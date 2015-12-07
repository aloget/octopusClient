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

    @Column(name = "senderId")
    public int senderId;

    @Column(name = "recipientId")
    public int recipientId;

    @Column(name = "message")
    public String message;

    @Column(name = "dispatchTimestamp")
    public long dispatchTimestamp;

    @Column(name = "messageIndex")
    public int messageIndex;

    public Message() {
        super();
    }

    public Message(int messageId, int senderId, int recipientId, String message, long dispatchTimestamp, int messageIndex) {
        super();
        this.messageId = messageId;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.message = message;
        this.dispatchTimestamp = dispatchTimestamp;
        this.messageIndex = messageIndex;
    }

    public void createOrUpdate() {
        Message message = Message.getMessage(this.messageId);
        if (message != null) {
            message.messageId = this.messageId;
            message.senderId = this.senderId;
            message.recipientId = this.recipientId;
            message.message = this.message;
            message.dispatchTimestamp = this.dispatchTimestamp;
            message.messageIndex = this.messageIndex;
            message.save();
        } else {
            this.save();
        }
    }

    public static List<Message> getMessages(int firstUserId, int secondUserId) {
        return new Select()
                .from(Message.class)
                .where("(senderId = ? and recipientId = ?) or (senderId = ? and recipientId = ?)",
                        firstUserId, secondUserId, secondUserId, firstUserId)
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
            int senderId = json.getInt("sender_id");
            int recipientId = json.getInt("recipient_id");
            String message = json.getString("message");
            int dispatchTimestamp = json.getInt("dispatch_timestamp");
            int messageIdx = json.getInt("message_index");
            return new Message(id, senderId, recipientId, message, dispatchTimestamp, messageIdx);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
