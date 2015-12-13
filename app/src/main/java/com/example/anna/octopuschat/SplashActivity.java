package com.example.anna.octopuschat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.anna.octopuschat.API.APIManager;
import com.example.anna.octopuschat.dbTables.Profile;
import com.example.anna.octopuschat.interfaces.AuthorizeListener;

import java.util.concurrent.TimeUnit;

public class SplashActivity extends Activity implements AuthorizeListener {


    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void dispatchMessage(android.os.Message msg) {
            super.dispatchMessage(msg);
            Profile profile = Profile.getProfile();
            if (profile != null) {
                APIManager.getInstance().authorize(profile.username, profile.password, SplashActivity.this);
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        handler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public void onBackPressed() {}

    @Override
    public void onAuthorizeSuccess() {
        startActivity(new Intent(this, UsersActivity.class));
        finish();
    }

    @Override
    public void onAuthorizeFailure(int code, String errorMessage) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
