package com.education.xeal;

import android.app.Application;

import com.education.xeal.utils.AppEnvironment;

public class BaseApplication extends Application {

    AppEnvironment appEnvironment;

    @Override
    public void onCreate() {
        super.onCreate();
        appEnvironment = AppEnvironment.PRODUCTION;
    }

    public AppEnvironment getAppEnvironment() {
        return appEnvironment;
    }

    public void setAppEnvironment(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
    }
}