package cn.skyui.library.http;

import android.app.Application;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.skyui.library.http.converter.FastJsonConverterFactory;
import cn.skyui.library.http.cookie.PersistentCookieJar;
import cn.skyui.library.http.cookie.cache.SetCookieCache;
import cn.skyui.library.http.cookie.persistence.SharedPrefsCookiePersistor;
import cn.skyui.library.http.interceptor.HttpCacheInterceptor;
import cn.skyui.library.http.interceptor.HttpHeaderInterceptor;
import cn.skyui.library.http.utils.HttpsUtils;
import cn.skyui.library.utils.Utils;
import okhttp3.Cache;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class HttpService {

    private static final String BASE_URL = "https://hkapp.foundersc.com/";

    private static final long DEFAULT_READ_TIMEOUT = 15;
    private static final long DEFAULT_WRITE_TIMEOUT = 20;
    private static final long DEFAULT_CONNECT_TIMEOUT = 20;
    private static final long DEFAULT_CACHE_SIZE = 10 * 1024 * 1024; // 10MB

    private static Application application;
    private static boolean httpDnsSwitch;

    private static HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(new StethoInterceptor()) // Facebook强大的监测工具：Stetho
            .addInterceptor(new HttpHeaderInterceptor())    // 添加通用的Header
            .addInterceptor(new HttpCacheInterceptor())
            .hostnameVerifier(new HttpsUtils.UnSafeHostnameVerifier())
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
            .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .cache(new Cache(Utils.getApp().getCacheDir(), DEFAULT_CACHE_SIZE))
            .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Utils.getApp()))) // 持久化cookie到本地（服务器控制cookie、免登陆）
            .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)) //支持HTTPS，明文Http与比较新的Https
            .build();

    /**
     * 通过此方法创建ApiService：
     * 注意：createService自动包装RxJava适配器，如果不使用RxJava请使用createServiceOriginal
     * Create diff module ApiService
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T createService(Class<T> serviceClass) {
        return createService(serviceClass, httpClient);
    }

    public static <T> T createService(Class<T> serviceClass, OkHttpClient customHttpClient) {
        if(customHttpClient == null) {
            customHttpClient = httpClient;
        }
        String baseUrl = "";
        try {
            Field field1 = serviceClass.getField("BASE_URL");
            baseUrl = (String) field1.get(serviceClass);
        } catch (Exception ignored) {
            baseUrl = BASE_URL;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(customHttpClient)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }

    /**
     * 使用原始Retrofit创建请求Service（不使用RxJava）
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T createServiceOriginal(Class<T> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }

    private static class OkHttpLogger implements Interceptor {

        private static final String TAG = OkHttpLogger.class.getSimpleName();

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Logger.v(TAG, "request:" + request.toString());
            long t1 = System.nanoTime();
            okhttp3.Response response = chain.proceed(chain.request());
            long t2 = System.nanoTime();
            Logger.v(TAG, String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            Logger.i(TAG, "response body:" + content);
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    }

    public static OkHttpClient getHttpClient() {
        return httpClient;
    }

    public static Application getApplication() {
        return application;
    }
}
