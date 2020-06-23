package com.example.hanall.base;

import android.app.Application;
import android.content.Context;

import com.example.hanall.utils.SpUtils;

public class App extends Application {

    private static App app;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        //初始化sputils
        SpUtils.init(getContext());

    }

    public static App getInstance() {
        return app;
    }

    public static Context getContext(){
        return getInstance().getApplicationContext();
    }
}
