package com.jannick.oslinux.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jannick.oslinux.R;
import com.jannick.oslinux.utils.LayoutUtil;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        LayoutUtil.changeStatusbarColor(this,R.color.colorPrimary);

        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 0.5 seconds
                    sleep(1000);

                    // After 1 seconds redirect to another intent
                    loadActivity();

                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }

    private void loadActivity(){
            LayoutUtil.navigateToActivity(this, MainActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
