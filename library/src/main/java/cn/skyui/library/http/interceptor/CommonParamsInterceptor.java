package cn.skyui.library.http.interceptor;

import java.io.IOException;

import cn.skyui.library.utils.AppUtils;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求 公共参数 拦截器
 */
public class CommonParamsInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (request.method().equals("GET")) {
            HttpUrl httpUrl = request.url().newBuilder()
                    .addQueryParameter("vc", AppUtils.getAppVersionCode() + "")
//                    .addQueryParameter("timestamp", String.valueOf(System.currentTimeMillis()))
                    .build();
            request = request.newBuilder().url(httpUrl).build();
        } else if (request.method().equals("POST")) {
            if (request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();

                for (int i = 0; i < formBody.size(); i++) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                }

                formBody = bodyBuilder
                        .addEncoded("version", AppUtils.getAppVersionCode() + "")
//                        .addEncoded("timestamp", String.valueOf(System.currentTimeMillis()))
                        .build();

                request = request.newBuilder().post(formBody).build();
            }
        }

        return chain.proceed(request);
    }

}
