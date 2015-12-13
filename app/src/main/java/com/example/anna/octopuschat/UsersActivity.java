package com.example.anna.octopuschat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.anna.octopuschat.API.APIManager;
import com.example.anna.octopuschat.adapters.UserAdapter;
import com.example.anna.octopuschat.dbTables.Profile;
import com.example.anna.octopuschat.dbTables.User;
import com.example.anna.octopuschat.interfaces.UsersListener;

/**
 * Created by anna on 06/12/15.
 */
public class UsersActivity extends Activity implements AdapterView.OnItemClickListener, UsersListener {
    private ListView listView;
    private UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        if (APIManager.isConnectionAvailable()) {
            APIManager.getInstance().getUsers(this);
        } else {
            onGetUsersSuccess();
        }
    }

    @Override
    public void onGetUsersSuccess() {
        mUserAdapter = new UserAdapter(this, User.getUsers());
        listView.setAdapter(mUserAdapter);
    }

    @Override
    public void onGetUsersFailure(int code, String errorString) {
        if (User.hasAtLeastOneEntity()) {
            onGetUsersSuccess();
        } else {
            Toast.makeText(this, "Не удалось получить данные с сервера!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_users, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, ChatActivity.class)
                .putExtra("contact_id", mUserAdapter.getItem(position).userId));
    }

    private void logout() {
        Profile.removeProfile();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
