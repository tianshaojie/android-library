package cn.skyui.library.web.javascript.bridge;

import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.orhanobut.logger.Logger;

import cn.skyui.library.utils.Utils;

public class ToastHandler implements BridgeHandler {

    public static final String NAME = "toast";

    @Override
    public void handler(String data, CallBackFunction function) {
        Logger.i("handler = toast, data from web = " + data);
        if(data != null) {
            Toast.makeText(Utils.getApp(), data, Toast.LENGTH_SHORT).show();
        }
    }

}
