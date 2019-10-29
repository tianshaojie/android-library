package cn.skyui.app.main.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import cn.skyui.app.R;
import cn.skyui.library.base.fragment.BaseFragment;

public class SubWebViewFragment extends BaseFragment {

    private static final String TAG = SubWebViewFragment.class.getSimpleName();

    public static SubWebViewFragment newInstance() {
        return new SubWebViewFragment();
    }

    private Handler handler;
    private View rootView;
    private WebView webView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String oriUrl = "https://sspai.com/post/57198";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return rootView = inflater.inflate(R.layout.fragment_webview_demo, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadData();
    }

    private void initView() {
        handler = new Handler(Looper.getMainLooper());
        webView = rootView.findViewById(R.id.webview);
        webView.setWebViewClient(new LoadingAndErrorWebViewClient());
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            loadData();
        });
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void loadData() {
        webView.loadUrl(oriUrl);
    }


    public void showError() {
        Toast.makeText(getActivity(), "加载失败，下拉重试", Toast.LENGTH_LONG).show();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void showSuccess() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //====================================================================

    // Flag to track that a loadUrl timeout occurred
    private int loadUrlTimeout = 0;
    private static final int DEFALUT_TIMEOUT = 10 * 1000; // 10s
    private boolean isLoading;

    private void clearLoadTimeoutTimer() {
        loadUrlTimeout++;
    }

    /**
     * 加载超时判断Runnable
     */
    class TimeoutRunnable implements Runnable {

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
                        webView.stopLoading();
                        Toast.makeText(getActivity(), "H5加载超时：" + oriUrl, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    /**
     * 带超时判断的WebViewClient
     */
    public class LoadingAndErrorWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            isLoading = true;
            Log.i(TAG, "onPageStarted: isLoading = " + isLoading);
            showLoading();
            final int currentLoadUrlTimeout = loadUrlTimeout;
            CheckTimeoutThreadPool.execute(new TimeoutRunnable(currentLoadUrlTimeout));
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
