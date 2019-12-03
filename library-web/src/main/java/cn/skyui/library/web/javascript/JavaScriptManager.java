package cn.skyui.library.web.javascript;

import android.content.Context;

import com.github.lzyzsd.jsbridge.BridgeHandler;

import java.util.HashMap;

import cn.skyui.aidl.IWebViewInterface;
import cn.skyui.library.web.javascript.bridge.IsLoginHandler;
import cn.skyui.library.web.javascript.bridge.ToastHandler;

/**
 * @author tianshaojie
 * @date 2018/1/15
 */
public class JavaScriptManager {

    public HashMap<String, BridgeHandler> handlers = new HashMap<>();

    private Context mContext;

    public JavaScriptManager(Context context) {
        this.mContext = context;
        handlers.put(ToastHandler.NAME, new ToastHandler());
    }

    /**
     * 需要调用主进程接口的js，在添加到handlers，通过webViewInterface做关联
     * @param webViewInterface WebView AIDL 在主进程的接口实现类实例
     */
    public void setWebViewInterface(IWebViewInterface webViewInterface) {
        handlers.put(IsLoginHandler.NAME, new IsLoginHandler(webViewInterface));
    }
}
