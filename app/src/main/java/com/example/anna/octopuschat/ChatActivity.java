package com.example.anna.octopuschat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.anna.octopuschat.API.APIManager;
import com.example.anna.octopuschat.adapters.MessageAdapter;
import com.example.anna.octopuschat.dbTables.Message;
import com.example.anna.octopuschat.dbTables.Profile;
import com.example.anna.octopuschat.dbTables.User;
import com.example.anna.octopuschat.interfaces.MessagesListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by anna on 06/12/15.
 */
public class ChatActivity extends Activity implements MessagesListener {

    private ListView listView;
    private MessageAdapter listViewAdapter;
    private User companion;
    private EditText editTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        int companion_id = getIntent().getIntExtra("companion_id", -1);
        companion = User.getUser(companion_id);

        listView = (ListView) findViewById(R.id.listView2);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        Button postMessageButton = (Button)findViewById(R.id.buttonPost);
        postMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextMessage.getText().length() > 0) {
                    postMessage();
                }
            }
        });

        getMessages();
        if (APIManager.isConnectionAvailable()) {
            getMessagesFromServer();
        } else {
            Toast.makeText(this, "Соединение недоступно.", Toast.LENGTH_LONG).show();
        }

        Timer timer = new Timer();
        TimerTask timerTask;

        timerTask = new TimerTask() {
            public void run() {
                getMessagesFromServer();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 2000);

        listViewAdapter = new MessageAdapter(this, getMessages());
        listView.setAdapter(listViewAdapter);
    }

    private List<MessageAdapter.Item> getMessages() {
        List<MessageAdapter.Item> result = new ArrayList<>();
        List<Message> messageList = Message.getMessages(Profile.getProfile().userId, companion.userId);
        for (Message message : messageList) {
            User user = User.getUser(message.senderId);
            MessageAdapter.Item item = new MessageAdapter.Item(message.getId().intValue(), user, message.message, message.dispatchTimestamp);
            result.add(item);
        }
        return result;
    }

    private void updateMessages(ArrayList<Message>messages) {
        for (Message newMessage : messages) {
            User user = User.getUser(newMessage.senderId);
            MessageAdapter.Item item = new MessageAdapter.Item(newMessage.getId().intValue(), user, newMessage.message, newMessage.dispatchTimestamp);
            listViewAdapter.add(item);
        }
    }

    private void getMessagesFromServer() {
        APIManager.getInstance().getMessages(companion.userId, companion.lastMessageIndex, this);
    }

    private void postMessage() {
            APIManager.getInstance().postMessage(companion.userId, companion.lastMessageIndex,
                    editTextMessage.getText().toString(), this);
    }

    @Override
    public void onGetMessagesSuccess(ArrayList<Message> messages) {
        updateMessages(messages);
    }

    @Override
    public void onGetMessagesFailure(int code, String errorMessage) {
        Toast.makeText(this, "Ошибка получения сообщений.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPostMessagesSuccess(ArrayList<Message> messages) {
        updateMessages(messages);
    }

    @Override
    public void onPostMessagesFailure(int code, String errorMessage) {
        Toast.makeText(this, "Ошибка отправки сообщения.", Toast.LENGTH_LONG).show();
    }
}
