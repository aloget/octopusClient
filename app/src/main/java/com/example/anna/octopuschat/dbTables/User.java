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

@Table(name = "users")
public class User extends Model {
    @Column(name = "userId")
    public int userId;

    @Column(name = "username")
    public String username;

    @Column(name = "lastMessageIndex")
    public int lastMessageIndex;

    public User() {};

    public User(int userId, String username, int lastMessageIndex) {
        this.userId = userId;
        this.username = username;
        this.lastMessageIndex = lastMessageIndex;
    }


    public void createOrUpdate() {
        User user = User.getUser(this.userId);
        if (user != null) {
            user.userId = this.userId;
            user.username = this.username;
            user.lastMessageIndex = this.lastMessageIndex;
            user.save();
        } else {
            this.save();
        }
    }

    public static List<User> getUsers() {
        return new Select()
                .from(User.class)
                .execute();
    }

    public static User getUser(int userId) {
        return new Select()
                .from(User.class)
                .where("userId = ?", userId)
                .executeSingle();
    }

    public static User getUser(String username) {
        return new Select()
                .from(User.class)
                .where("username = ?", username)
                .executeSingle();
    }

    public static User getUser(JSONObject json) {
        try {
            int id = json.getInt("id");
            String username = json.getString("username");
            int lastMessageIdx = json.optInt("last_message_index", 0);
            return new User(id, username, lastMessageIdx);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasAtLeastOneEntity() {
        return getUsers().size() > 0;
    }

}
