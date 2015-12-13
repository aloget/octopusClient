package com.example.anna.octopuschat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anna.octopuschat.API.APIManager;
import com.example.anna.octopuschat.interfaces.AuthorizeListener;
import com.example.anna.octopuschat.interfaces.RegisterListener;

public class MainActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener, RegisterListener, AuthorizeListener {
    private EditText et_username;
    private EditText et_password;
    private Button btn_register;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.NoActionBarAppTheme);
        setContentView(R.layout.activity_main);
        btn_login = (Button) findViewById(R.id.bt_login);
        btn_register = (Button) findViewById(R.id.bt_register);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        et_username.setOnFocusChangeListener(this);
        et_password.setOnFocusChangeListener(this);
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_username:
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_username.getWindowToken(), 0);
                }
                break;
            case R.id.et_password:
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_password.getWindowToken(), 0);
                }
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


    @Override
    public void onRegisterSuccess() {
        startActivity(new Intent(this, UsersActivity.class));
        finish();
    }

    @Override
    public void onRegisterFailure(int code, String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthorizeSuccess() {
        startActivity(new Intent(this, UsersActivity.class));
        finish();
    }

    @Override
    public void onAuthorizeFailure(int code, String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}