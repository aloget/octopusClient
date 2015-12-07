package com.example.anna.octopuschat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anna.octopuschat.API.APIManager;
import com.example.anna.octopuschat.interfaces.AuthorizeListener;
import com.example.anna.octopuschat.interfaces.RegisterListener;

public class MainActivity extends Activity implements View.OnClickListener, RegisterListener, AuthorizeListener {


    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                authorize();
                break;
            case R.id.buttonRegister:
                register();
                break;
        }
    }

    private void authorize() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (username.length() > 0 && password.length() > 0) {
            APIManager.getInstance().authorize(username, password, this);
        }
    }

    private void register() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (username.length() > 0 && password.length() > 0) {
            APIManager.getInstance().register(username, password, this);
        }
    }

    private void startUsersActivity() {
        startActivity(new Intent(this, UsersActivity.class));
        finish();
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
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

}
