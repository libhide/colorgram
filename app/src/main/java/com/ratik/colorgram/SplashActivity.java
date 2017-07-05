package com.ratik.colorgram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    public static final String CONST_FIRST_RUN = "first_run";
    private boolean isFirstRun = true;
    private Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mainIntent = new Intent(SplashActivity.this, MainActivity.class);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        isFirstRun = sp.getBoolean(SplashActivity.CONST_FIRST_RUN, true);

        if (isFirstRun) {
            showSplashScreen();
        } else {
            startActivity(mainIntent);
        }
    }

    private void showSplashScreen() {
        setContentView(R.layout.activity_splash);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
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
