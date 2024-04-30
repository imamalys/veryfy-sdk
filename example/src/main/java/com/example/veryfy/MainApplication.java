package com.example.veryfy;

import android.app.Application;
import android.content.Context;

import com.ionnex.veryfy.constants.APIConstant;
import com.ionnex.veryfy.utils.HttpSingleton;

public class MainApplication extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        HttpSingleton.getInstance(context);
        APIConstant.API_KEY = BuildConfig.API_KEY;
    }
}
