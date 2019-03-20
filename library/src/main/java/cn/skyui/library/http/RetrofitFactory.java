package cn.skyui.library.http;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import cn.skyui.library.http.converter.FastJsonConverterFactory;
import cn.skyui.library.http.cookie.PersistentCookieJar;
import cn.skyui.library.http.cookie.cache.SetCookieCache;
import cn.skyui.library.http.cookie.persistence.SharedPrefsCookiePersistor;
import cn.skyui.library.http.interceptor.CommonParamsInterceptor;
import cn.skyui.library.http.interceptor.HttpCacheInterceptor;
import cn.skyui.library.http.interceptor.HttpHeaderInterceptor;
import cn.skyui.library.http.utils.HttpsUtils;
import cn.skyui.library.utils.Utils;
import okhttp3.Cache;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitFactory {

    public static final String BASE_URL = "http://preview.skyui.cn/";

    private static final long DEFAULT_READ_TIMEOUT = 15;
    private static final long DEFAULT_WRITE_TIMEOUT = 20;
    private static final long DEFAULT_CONNECT_TIMEOUT = 20;
    private static final long DEFAULT_CACHE_SIZE = 10 * 1024 * 1024; // 10MB

    private static HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
    public static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(new StethoInterceptor()) // Facebook强大的监测工具：Stetho
            .addInterceptor(new HttpHeaderInterceptor())    // 添加通用的Header
            .addInterceptor(new CommonParamsInterceptor())
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
     * 通过此方法创建ApiService：RxJava版本
     * 注意：createService自动包装RxJava适配器，如果不使用RxJava请使用createServiceOriginal
     * Create diff module ApiService
     * @param serviceClass 通常是接口类 ApiService.class
     * @param <T> 接口泛型
     * @return Retrofit 接口
     */
    public static <T> T createService(Class<T> serviceClass) {
        String baseUrl = "";
        try {
            Field field1 = serviceClass.getField("BASE_URL");
            baseUrl = (String) field1.get(serviceClass);
        } catch (Exception ignored) {
            baseUrl = BASE_URL;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }

    /**
     * Retrofit原始Service
     * @param serviceClass 通常是接口类 ApiService.class
     * @param <T> 接口泛型
     * @return Retrofit 接口
     */
    public static <T> T createServiceOriginal(Class<T> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }
}
