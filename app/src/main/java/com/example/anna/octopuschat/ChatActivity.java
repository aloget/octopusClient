package com.example.anna.octopuschat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
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

/**
 * Created by anna on 06/12/15.
 */
public class ChatActivity extends Activity implements MessagesListener {
    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(android.os.Message msg) {
            super.dispatchMessage(msg);
            getMessagesFromServer();
            if (!isDestroy)
                handler.sendEmptyMessageDelayed(0, 2000);
        }
    };
    private ListView listView;
    private MessageAdapter messageAdapter;
    private User mContact;
    private EditText editTextMessage;
    private boolean isDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        int contact_id = getIntent().getIntExtra("contact_id", -1);
        mContact = User.getUser(contact_id);
        getActionBar().setTitle(mContact.username);

        listView = (ListView) findViewById(R.id.listView2);
        editTextMessage = (EditText) findViewById(R.id.et_message);

        Button postMessageButton = (Button) findViewById(R.id.bt_post);
        postMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextMessage.getText().length() > 0) {
                    postMessage();
                    editTextMessage.getText().clear();
                }
            }
        });

        if (APIManager.isConnectionAvailable()) {
            handler.sendEmptyMessage(0);
        } else {
            Toast.makeText(this, "Интернет-соединение отсутствует.", Toast.LENGTH_LONG).show();
        }

        messageAdapter = new MessageAdapter(this, getMessages());
        listView.setAdapter(messageAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    private List<MessageAdapter.Item> getMessages() {
        List<MessageAdapter.Item> result = new ArrayList<>();
        List<Message> messageList = Message.getMessages(mContact.userId);
        for (Message message : messageList) {
            String username = message.inbox ? User.getUser(message.contactId).username : Profile.getProfile().username;
            MessageAdapter.Item item = new MessageAdapter.Item(message.getId().intValue(), username, message.message, message.dispatchTimestamp, message.inbox);
            result.add(item);
        }
        return result;
    }

    private void updateMessages(ArrayList<Message> messages) {
        for (Message newMessage : messages) {
            String username = newMessage.inbox ? User.getUser(newMessage.contactId).username : Profile.getProfile().username;
            MessageAdapter.Item item = new MessageAdapter.Item(newMessage.getId().intValue(), username, newMessage.message, newMessage.dispatchTimestamp, newMessage.inbox);
            messageAdapter.add(item);
        }
    }

    private void getMessagesFromServer() {
        APIManager.getInstance().getMessages(mContact.userId, mContact.lastMessageIndex, this);
    }

    private void postMessage() {
        APIManager.getInstance().postMessage(mContact.userId, mContact.lastMessageIndex,
                editTextMessage.getText().toString(), this);
    }

    @Override
    public void onGetMessagesSuccess(ArrayList<Message> messages) {
        updateMessages(messages);
    }

    @Override
    public void onGetMessagesFailure(int code, String errorMessage) {
        Toast.makeText(this, errorMessage + " " + String.valueOf(code), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPostMessagesSuccess(ArrayList<Message> messages) {
        updateMessages(messages);
    }

    @Override
    public void onPostMessagesFailure(int code, String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}
