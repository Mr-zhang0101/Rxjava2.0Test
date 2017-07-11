package com.zhang.rxjavademo;

import android.app.Application;
import android.content.Context;

import com.zhang.rxjavademo.api.RetrofitService;

/**
 * Project: RxjavaDemo
 * Author:  张佳林
 * Version:  1.0
 * Date:    2017/7/10
 * Modify:  //TODO
 * Description: //TODO
 * Copyright notice:
 */

public class MyApp extends Application{
    public static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        RetrofitService.init();
    }
    public static Context getAppContext(){
        return sContext;
    }
}
