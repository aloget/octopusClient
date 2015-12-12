package com.example.anna.octopuschat.dbTables;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by anna on 07/12/15.
 */
@Table(name = "profiles")
public class Profile extends Model {
    private static Profile sProfile;

    @Column(name = "userId")
    public int userId;

    @Column(name = "username")
    public String username;

    @Column(name = "password")
    public String password;

    @Column(name = "token")
    public String token;

    public Profile() {
        super();
    }

    public Profile(int userId, String username, String password, String token) {
        super();
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public static Profile getProfile() {
        if (sProfile == null) {
            sProfile = new Select()
                    .from(Profile.class)
                    .executeSingle();
        }
        return sProfile;
    }

    public static void removeProfile() {
        getProfile().delete();

        for (User user : User.getUsers()) {
            user.delete();
        }
        for (Message message : Message.getMessages()) {
            message.delete();
        }

        sProfile = null;
    }
}