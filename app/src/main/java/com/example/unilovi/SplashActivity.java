package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.utils.CallBack;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseUser user = Firebase.getUsuarioActual();

        if (user != null && user.isEmailVerified())
            Firebase.existUser(new CallBack() {
                @Override
                public void methodToCallBack(Object object) {
                    if ((Boolean) object) {
                        showHome();
                    }
                    else {
                        showLogin();
                    }
                }
            });
        else {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            showLogin();
        }
    }

    private void showHome() {
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void showLogin() {
        Intent mainIntent = new Intent(SplashActivity.this, SignInActivity.class);
        startActivity(mainIntent);
        finish();
    }
}