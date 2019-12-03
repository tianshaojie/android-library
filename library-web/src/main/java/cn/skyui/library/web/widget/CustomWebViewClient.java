package cn.skyui.library.web.widget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.KeyEvent;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebViewClient;

import cn.skyui.library.web.activity.WebViewActivity;


/**
 * @author tianshaojie
 * @date 2018/2/9
 */
public class CustomWebViewClient extends BridgeWebViewClient {

    private Activity mWebViewActivity;
    private CustomWebView mWebView;

    public CustomWebViewClient(Activity mainActivity, CustomWebView mWebView) {
        super(mWebView);
        mWebViewActivity = mainActivity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        try {
            mWebViewActivity.setTitle(view.getTitle());
            mWebView.setCurrentUrl(url);
            view.clearCache(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        try {
            mWebViewActivity.setTitle(description);
            if (errorCode == -10) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(failingUrl));
                try {
                    mWebViewActivity.startActivity(intent);
                    mWebViewActivity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
