package cn.skyui.library.web.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import cn.skyui.aidl.IWebViewInterface;
import cn.skyui.library.utils.AppUtils;
import cn.skyui.library.utils.NetworkUtils;
import cn.skyui.library.utils.ToastUtils;
import cn.skyui.library.utils.threadpool.AppThreadPool;
import cn.skyui.library.utils.threadpool.PriorityRunnable;
import cn.skyui.library.web.R;
import cn.skyui.library.web.activity.WebViewActivity;
import cn.skyui.library.web.javascript.JavaScriptManager;

/**
 * @author tianshaojie
 *
 * WebView布局封装：包含加载状态布局、失败状态布局、超时失败逻辑
 * 直接在xml内引用 WithLoadingAndErrorWebView
 */
public class WithLoadingAndErrorWebView extends FrameLayout {

    private static final String TAG = WithLoadingAndErrorWebView.class.getSimpleName();

    private Context mContext;
    private Handler handler;

    protected CustomWebView mWebView;
    private LoadErrorView mLoadErrorView;
    private LoadingView mLoadingView;
    private String oriUrl;

    // 默认10s加载超时显示失败页面
    private static final int DEFALUT_TIMEOUT = 10 * 1000;
    // Flag to track that a loadUrl timeout occurred
    private int loadUrlTimeout = 0;
    private boolean isLoading;

    private JavaScriptManager javaScriptManager;
    private IWebViewInterface webViewInterface;

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.i("onServiceConnected");
            if(service == null) {
                Logger.e("IBinder service is null");
                return;
            }
            webViewInterface = IWebViewInterface.Stub.asInterface(service);
            if(webViewInterface == null) {
                Logger.e("Stub.asInterface, webViewInterface is null");
                return;
            }
            javaScriptManager.setWebViewInterface(webViewInterface);
            for (String key : javaScriptManager.handlers.keySet()) {
                mWebView.registerHandler(key, javaScriptManager.handlers.get(key));
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.i("onServiceDisconnected");
            webViewInterface = null;
        }
    };

    public WithLoadingAndErrorWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public WithLoadingAndErrorWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WithLoadingAndErrorWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WithLoadingAndErrorWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        handler = new Handler(Looper.getMainLooper());
        View view = inflate(getContext(), R.layout.layout_webview_with_loading_and_error, this);
        // WebView
        mWebView = view.findViewById(R.id.fz_webview);
        mWebView.setWebViewClient(new LoadingAndErrorWebViewClient((Activity) getContext(), mWebView));
        mWebView.setWebChromeClient(new CustomWebChromeClient((Activity) getContext()));

        // 加载中布局，失败状态布局
        mLoadingView = view.findViewById(R.id.layout_loading_view);
        mLoadErrorView = view.findViewById(R.id.layout_error_view);
        mLoadErrorView.setReloadListener(new LoadErrorView.ReloadListener() {
            @Override
            public void reload() {
                if (NetworkUtils.isConnected()) {
                    showLoading();
                    mWebView.loadUrl(oriUrl);
                }
            }
        });

        if(!NetworkUtils.isConnected()) {
            showError();
        }

        javaScriptManager = new JavaScriptManager(getContext());
        bindService();
        // 初始化JS调用方法
        for (String key : javaScriptManager.handlers.keySet()) {
            mWebView.registerHandler(key, javaScriptManager.handlers.get(key));
        }
    }

    private void bindService() {
        Intent service = new Intent(WebViewActivity.ACTION);
        service.setPackage(getContext().getPackageName());
        mContext.bindService(service, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void loadUrl(String url) {
        this.oriUrl = url;
        if(!TextUtils.isEmpty(oriUrl)) {
            mWebView.loadUrl(oriUrl);
        } else {
            if(AppUtils.isAppDebug()) {
                ToastUtils.showShort("URL为空");
            }
            showError();
        }
    }

    public void reload() {
        if(!TextUtils.isEmpty(oriUrl)) {
            mWebView.reload();
        } else {
            if(AppUtils.isAppDebug()) {
                ToastUtils.showShort("URL为空");
            }
            showError();
        }
    }

    public void showError() {
        mLoadErrorView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
    }

    private void showLoading() {
        mLoadingView.setVisibility(VISIBLE);
        mLoadErrorView.setVisibility(GONE);
    }

    private void showSuccess() {
        mLoadErrorView.setVisibility(GONE);
        mLoadingView.setVisibility(GONE);
    }

    private void clearLoadTimeoutTimer() {
        loadUrlTimeout++;
    }



    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mWebView != null) {
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }

            mWebView.stopLoading();
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.mProgressBar = null;
            mWebView.destroy();
            mWebView = null;
        }
        if (webViewInterface != null) {
            getContext().unbindService(mServiceConnection);
        }
    }

    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    public void goBack() {
        mWebView.goBack();
    }

    public String getCurrentUrl() {
        return mWebView.getCurrentUrl();
    }

    //====================================================================

    /**
     * 加载超时判断Runnable
     */
    class TimeoutRunnable extends PriorityRunnable {

        private int currentLoadUrlTimeout;

        TimeoutRunnable(int currentLoadUrlTimeout) {
            this.currentLoadUrlTimeout = currentLoadUrlTimeout;
        }

        @Override
        public void run() {
            Log.i(TAG, "timeout: begin currentLoadUrlTimeout = " + currentLoadUrlTimeout);
            try {
                synchronized (this) {
                    wait(DEFALUT_TIMEOUT);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 通知加载超时，停止加载
            Log.i(TAG, "timeout: end loadUrlTimeout = " + loadUrlTimeout);
            if (loadUrlTimeout == currentLoadUrlTimeout) {
                Log.i(TAG, "timeout: handler.post");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        clearLoadTimeoutTimer();
                        // 超时显示错误页面
                        showError();
                        isLoading = false;
                        mWebView.stopLoading();
                        if(AppUtils.isAppDebug()) {
                            Toast.makeText(mContext, "H5加载超时：" + oriUrl, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    /**
     * 带超时判断的WebViewClient
     */
    public class LoadingAndErrorWebViewClient extends CustomWebViewClient {

        LoadingAndErrorWebViewClient(Activity activity, CustomWebView mWebView) {
            super(activity, mWebView);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            isLoading = true;
            Log.i(TAG, "onPageStarted: isLoading = " + isLoading);
            showLoading();
            final int currentLoadUrlTimeout = loadUrlTimeout;
            AppThreadPool.getInstance().execute(new TimeoutRunnable(currentLoadUrlTimeout));
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i(TAG, "onPageFinished: isLoading = " + isLoading);
            if(!isLoading) {
                return;
            }
            isLoading = false;
            showSuccess();
            clearLoadTimeoutTimer();
            super.onPageFinished(view, url);
        }

        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            if(!isLoading) {
                return;
            }
            isLoading = false;
            clearLoadTimeoutTimer();
            // 加isForMainFrame判断是为了过滤一些非unrecoverable的错误。
            // 因为在Android6以上的机器上，网页中的任意一个资源获取不到（比如字体），网页就很可能显示错误界面，导致用户体验教差。
            if (request.isForMainFrame()) {
                // 这个方法在6.0才出现
                if (isShowError(request.getUrl().toString(), errorResponse.getStatusCode())) {
                    showError();
                    return;
                }
            }
            super.onReceivedHttpError(view, request, errorResponse);
        }

        boolean isShowError(String url, int statusCode) {
            return 404 == statusCode || 500 == statusCode;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.i(TAG, "onReceivedError: isLoading = " + isLoading);
            if(!isLoading) {
                return;
            }
            isLoading = false;
            clearLoadTimeoutTimer();
            if ((errorCode == WebViewClient.ERROR_BAD_URL
                    || errorCode == WebViewClient.ERROR_HOST_LOOKUP
                    || errorCode == WebViewClient.ERROR_CONNECT
                    || errorCode == ERROR_TIMEOUT)) {
                showError();
                return;
            }
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @SuppressLint("NewApi")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }
    }

}
