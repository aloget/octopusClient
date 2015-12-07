package com.example.anna.octopuschat.interfaces;

import com.example.anna.octopuschat.dbTables.Message;

import java.util.ArrayList;

/**
 * Created by anna on 07/12/15.
 */
public interface MessagesListener {
    void onGetMessagesSuccess(ArrayList<Message> messages);
    void onGetMessagesFailure(int code, String errorMessage);
    void onPostMessagesSuccess(ArrayList<Message> messages);
    void onPostMessagesFailure(int code, String errorMessage);

}
