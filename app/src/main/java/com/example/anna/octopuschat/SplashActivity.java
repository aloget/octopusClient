package com.example.anna.octopuschat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.anna.octopuschat.API.APIManager;
import com.example.anna.octopuschat.dbTables.Profile;
import com.example.anna.octopuschat.interfaces.AuthorizeListener;

import java.util.concurrent.TimeUnit;

public class SplashActivity extends Activity implements AuthorizeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Profile profile = Profile.getProfile();
                if (profile != null) {
                    APIManager.getInstance().authorize(profile.username, profile.password, SplashActivity.this);
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
    }

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