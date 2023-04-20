package com.bct.fuelpay;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    public static MyApplication Instance() {
        return instance;
    }
}
