package cn.skyui.library.http.interceptor;

import java.io.IOException;
import java.util.HashMap;

import cn.skyui.library.utils.AppUtils;
import cn.skyui.library.utils.DeviceUtils;
import cn.skyui.library.utils.StringUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求 公共Header 拦截器
 */
public class HttpHeaderInterceptor implements Interceptor {

    private static HashMap<String, String> headers = new HashMap<>();

    static {
        headers.put("User-Agent", "Android-" + AppUtils.getAppVersionName() + "-" + AppUtils.getAppVersionCode());
        headers.put("Accept", "application/json");
        headers.put("Content-type", "application/json");
        headers.put("channel", "github");
        headers.put("version", AppUtils.getAppVersionCode() + "");
        headers.put("device", DeviceUtils.getAndroidID());
    }

    /**
     * 添加全局 Header
     * @param key key
     * @param value value
     */
    public static void addHeader(String key, String value) {
        if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
            headers.put(key, value);
        }
    }

    /**
     * 删除全局 Header
     * @param key key
     */
    public static void removeHeader(String key) {
        if (StringUtils.isNotEmpty(key)) {
            headers.remove(key);
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder();
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
                    requestBuilder.addHeader(key, value);
                }
            }
        }
        return chain.proceed(requestBuilder.build());
    }

}
