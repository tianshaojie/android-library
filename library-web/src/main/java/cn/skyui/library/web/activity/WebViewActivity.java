package cn.skyui.library.web.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.skyui.aidl.IWebViewInterface;
import cn.skyui.library.base.activity.BaseSwipeBackActivity;
import cn.skyui.library.utils.AppUtils;
import cn.skyui.library.web.R;
import cn.skyui.library.web.javascript.JavaScriptManager;
import cn.skyui.library.web.widget.CustomWebChromeClient;
import cn.skyui.library.web.widget.CustomWebView;
import cn.skyui.library.web.widget.CustomWebViewClient;
import cn.skyui.library.web.widget.WithLoadingAndErrorWebView;

/**
 * 1. 独立进程：AIDL通信，获取App用户态，调用主App分享界面等
 * 2. 使用JsBridge作为js和java通信：https://github.com/lzyzsd/JsBridge
 * 3. 进度条
 * 4. Title自动更新
 *
 * @author tianshaojie
 * @date 2018/1/15
 */
@Route(path = "/web/h5")
public class WebViewActivity extends BaseSwipeBackActivity {

    public static final String ACTION = "cn.skyui.aidl.IWebViewInterface";

    public static final String URL = "url";
    public static final String TITLE = "title";

    private WithLoadingAndErrorWebView withLoadingAndErrorWebView;
    private String sourceUrl;   // 打开页面时的URL

//    private JavaScriptManager javaScriptManager;
//    private IWebViewInterface webViewInterface;
//
//    ServiceConnection mServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            Logger.i("onServiceConnected");
//            if(service == null) {
//                Logger.e("IBinder service is null");
//                return;
//            }
//            webViewInterface = IWebViewInterface.Stub.asInterface(service);
//            if(webViewInterface == null) {
//                Logger.e("Stub.asInterface, webViewInterface is null");
//                return;
//            }
//            javaScriptManager.setWebViewInterface(webViewInterface);
//            for (String key : javaScriptManager.handlers.keySet()) {
//                mWebView.registerHandler(key, javaScriptManager.handlers.get(key));
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Logger.i("onServiceDisconnected");
//            webViewInterface = null;
//        }
//    };

    @Override
    protected void onCreateSafely(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_webview);
        setConfigCallback((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        Logger.i("WebViewActivity onCreateSafely...");
        initIntentData();
        if (sourceUrl == null || sourceUrl.length() == 0) {
            finish();
            return;
        }
//        javaScriptManager = new JavaScriptManager(mActivity);
//        bindService();
        initView();
    }


    private void initIntentData() {
        Intent intent = getIntent();
        sourceUrl = intent.getStringExtra(URL);
        if (sourceUrl == null || sourceUrl.length() == 0) {
            if(AppUtils.isAppDebug()) {
                Toast.makeText(this, "Url不能为空", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        String title = intent.getStringExtra(TITLE);
        if (title != null) {
            setTitle(title);
        }
    }

//    private void bindService() {
//        Intent service = new Intent(ACTION);
//        service.setPackage(getPackageName());
//        bindService(service, mServiceConnection, Context.BIND_AUTO_CREATE);
//    }

    private void initView() {
        withLoadingAndErrorWebView = findViewById(R.id.withLoadingAndErrorWebView);
        withLoadingAndErrorWebView.loadUrl(sourceUrl);

//        for (String key : javaScriptManager.handlers.keySet()) {
//            mWebView.registerHandler(key, javaScriptManager.handlers.get(key));
//        }
    }

    @Override
    protected void onDestroy() {
        setConfigCallback(null);
        super.onDestroy();
//        System.exit(0);
    }

    public void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);

            if (null == configCallback) {
                return;
            }

            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onBackClick();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onBackClick() {
        if (withLoadingAndErrorWebView != null && withLoadingAndErrorWebView.canGoBack()) {
            withLoadingAndErrorWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackClick();
            return true;
        } else if (id == R.id.action_refresh) {
            withLoadingAndErrorWebView.loadUrl(withLoadingAndErrorWebView.getCurrentUrl());
        } /*else if(id == R.id.action_share) {
            try {
                webViewInterface.invokeShare();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }*/ else if (id == R.id.action_copy) {
            ClipboardManager clipboard = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newUri(getApplication().getContentResolver(), "uri", Uri.parse(withLoadingAndErrorWebView.getCurrentUrl())));
        } else if (id == R.id.action_browser) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(withLoadingAndErrorWebView.getCurrentUrl())));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 使隐藏菜单项显示icon
     */
    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }
}