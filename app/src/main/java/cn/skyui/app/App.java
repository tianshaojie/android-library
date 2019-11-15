package cn.skyui.app;

import android.app.Application;

import com.orhanobut.logger.Logger;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i("onCreate");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Logger.i("onTerminate");
    }
}
