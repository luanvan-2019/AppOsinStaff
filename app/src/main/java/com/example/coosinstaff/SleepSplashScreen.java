package com.example.coosinstaff;

import android.app.Application;
import android.os.SystemClock;

public class SleepSplashScreen extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(2000);
    }
}
