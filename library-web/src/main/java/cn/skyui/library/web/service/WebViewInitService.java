package cn.skyui.library.web.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import cn.skyui.library.LibraryInitProvider;
import cn.skyui.library.base.activity.BaseActivity;

/**
 * @author tianshaojie
 * 创建H5进程，并初始化进程需要使用的库，完成初始化，会自动stopSelf();
 * 在SplashActivity内调用：
 * SplashActivity onCreate 调用 startHiddenService
 */
public class WebViewInitService extends Service {

    public static void startHiddenService(Context context){
        context.startService(new Intent(context, WebViewInitService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BaseActivity.APP_STATUS = BaseActivity.APP_STATUS_NORMAL;
        LibraryInitProvider.initUtils(getApplicationContext());
        LibraryInitProvider.initLogger();
        LibraryInitProvider.initMMKV();
        LibraryInitProvider.initNightMode();
        LibraryInitProvider.initRouter();
        stopSelf();
        Logger.i("WebViewInitService onCreate-----");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("WebViewInitService onDestroy-----");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}