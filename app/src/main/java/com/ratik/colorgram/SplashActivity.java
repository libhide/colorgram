package com.ratik.colorgram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        showSplashScreen();
    }

    private void showSplashScreen() {
        setContentView(R.layout.activity_splash);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(1500);
                    startActivity(mainIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
