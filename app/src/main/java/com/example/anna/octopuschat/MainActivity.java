package com.example.anna.octopuschat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anna.octopuschat.API.APIManager;
import com.example.anna.octopuschat.dbTables.Profile;
import com.example.anna.octopuschat.interfaces.AuthorizeListener;
import com.example.anna.octopuschat.interfaces.RegisterListener;

public class MainActivity extends Activity implements View.OnClickListener, RegisterListener, AuthorizeListener {
    private EditText et_username;
    private EditText et_password;

    private boolean isDestroy;

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (!isDestroy) {
                Profile profile = Profile.getProfile();
                if (profile != null) {
                    APIManager.getInstance().authorize(profile.username, profile.password, MainActivity.this);
                } else {
                    hideSplash();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.NoActionBarAppTheme);
        setContentView(R.layout.activity_main);
        Button bt_login = (Button) findViewById(R.id.bt_login);
        Button bt_register = (Button) findViewById(R.id.bt_register);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);

        bt_login.setOnClickListener(this);
        bt_register.setOnClickListener(this);

        handler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                authorize();
                break;
            case R.id.bt_register:
                register();
                break;
        }
    }

    private void authorize() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        if (username.length() > 0 && password.length() > 0) {
            APIManager.getInstance().authorize(username, password, this);
        }
    }

    private void register() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        if (username.length() > 0 && password.length() > 0) {
            APIManager.getInstance().register(username, password, this);
        }
    }

    private void startUsersActivity() {
        startActivity(new Intent(this, UsersActivity.class));
        finish();
    }

    private void hideSplash() {
        findViewById(R.id.rl_content).setVisibility(View.VISIBLE);
        findViewById(R.id.iv_splash).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRegisterSuccess() {
        startUsersActivity();
    }

    @Override
    public void onRegisterFailure(int code, String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthorizeSuccess() {
        startUsersActivity();
    }

    @Override
    public void onAuthorizeFailure(int code, String errorMessage) {
        hideSplash();
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}