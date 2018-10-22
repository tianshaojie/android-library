package cn.skyui.library.data;

import cn.skyui.library.http.HttpResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by tiansj on 2017/12/3.
 */

public interface ApiService {

//    @POST("v1/api1/aliyun/oss/token")
//    Observable<HttpResponse<String>> getOssToken();

    // 注意返回值：在 Retrofit 2中，每个请求被包装成一个 Call 对象
    @GET("v1/api1/aliyun/oss/token")
    Call<HttpResponse> getOssToken();
}
