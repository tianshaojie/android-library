package cn.skyui.library.web.javascript.bridge;

import android.os.RemoteException;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.orhanobut.logger.Logger;

import cn.skyui.aidl.IWebViewInterface;

public class IsLoginHandler implements BridgeHandler {

    public static final String NAME = "isLogin";

    private IWebViewInterface webViewInterface;

    public IsLoginHandler(IWebViewInterface webViewInterface) {
        this.webViewInterface = webViewInterface;
    }

    @Override
    public void handler(String data, CallBackFunction function) {
        Logger.i("handler = isLogin, data from web = " + data);
        if(webViewInterface == null) {
            Logger.e("webViewInterface is null!");
            return;
        }
        try {
            function.onCallBack(webViewInterface.isLogin() + "");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
