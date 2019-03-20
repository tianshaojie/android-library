package cn.skyui.library.http.interceptor;

import java.io.IOException;

import cn.skyui.library.utils.NetworkUtils;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求 缓存策略 拦截器
 */
public class HttpCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        // 无网络时，始终使用本地Cache
        if (!NetworkUtils.isConnected()) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);
        if (NetworkUtils.isConnected()) {
            // 有网络时，设置缓存过期时间0个小时
            int maxAge = 0;
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Pragma")
                    .build();
        } else {
            // 无网络时，设置缓存过期超时时间为4周
            int maxStale = 60 * 60 * 24 * 28;
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
        }
        return response;
    }

}
