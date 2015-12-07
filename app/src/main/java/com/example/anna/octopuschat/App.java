package com.example.anna.octopuschat;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.example.anna.octopuschat.API.APIManager;
import com.example.anna.octopuschat.dbTables.Message;
import com.example.anna.octopuschat.dbTables.Profile;
import com.example.anna.octopuschat.dbTables.User;

/**
 * Created by anna on 07/12/15.
 */
public class App extends Application {
    private static final String[] USERNAME = {"user1", "user2", "user3", "user4", "user5"};


    @Override
    public void onCreate() {
        super.onCreate();
        Configuration configuration = new Configuration.Builder(this)
                .addModelClasses(User.class, Message.class, Profile.class)
                .create();
        ActiveAndroid.initialize(configuration);

//        for (String username : USERNAME) {
//            if (User.getUser(username) == null) {
//                User user1 = new User();
//                user1.username = username;
//                user1.save();
//            }
//        }
//
//        new Message(2, 1, "from 2 to 1", 0, 0).save();
//        new Message(1, 2, "from 1 to 2", 0, 0).save();
//        new Message(2, 1, "from 2 to 1", 0, 0).save();
//        new Message(1, 2, "from 1 to 2", 0, 0).save();

        APIManager.init(this);

    }
}
