package cn.skyui.library.utils.oss;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.orhanobut.logger.Logger;
import cn.skyui.library.data.ApiService;
import cn.skyui.library.http.HttpResponse;
import cn.skyui.library.http.RetrofitFactory;
import cn.skyui.library.utils.Utils;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;

/**
 * Created by tiansj on 15/8/31.
 */
public class UploadClient {

    public static final String BASE_IMAGE_DOMAIN = "https://astatic.oss-cn-beijing.aliyuncs.com/";
    public static final String IMAGE_DOMAIN = "http://oss-cn-beijing.aliyuncs.com"; // 该目录下要有两个文件：file1m  file10m
    public static final String BUCKET_NAME = "astatic";

    static OSS oss;
    static {
        initOSS();
    }

    // 初始化OSSClient
    private static void initOSS() {
        OSSLog.enableLog();

        //推荐使用OSSAuthCredentialsProvider。token过期可以及时更新
        OSSFederationCredentialProvider ossFederationCredentialProvider = new OSSFederationCredentialProvider() {
            @Override
            public OSSFederationToken getFederationToken() {
               return getToken();
            }
        };

        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(Utils.getApp(), IMAGE_DOMAIN, ossFederationCredentialProvider);
    }

    private static OSSFederationToken getToken() {
        Call<HttpResponse> result = RetrofitFactory.createServiceOriginal(ApiService.class).getOssToken();
        try {
            HttpResponse response = result.execute().body();
            if(response != null && response.getCode() == 200) {
                JSONObject jsonObject = JSON.parseObject(response.getBody().toString());
                String ak = jsonObject.getString("AccessKeyId");
                String sk = jsonObject.getString("AccessKeySecret");
                String securityToken = jsonObject.getString("SecurityToken");
                long expireTime = jsonObject.getLong("Expiration");
                return new OSSFederationToken(ak, sk, securityToken, expireTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void asyncUpload(File file, final UploadCallbackHandler handler) {
        if(file == null) {
            return;
        }
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(BUCKET_NAME, file.getName(), file.getAbsolutePath());

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Logger.d("PutObject = " + "currentSize: " + currentSize + " totalSize: " + totalSize);
                handler.handleProgressMessage(request.getObjectKey(), currentSize, totalSize);
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Logger.d("PutObject = " + request.getObjectKey());
                Logger.d("ETagt = " + result.getETag());
                Logger.d("RequestIdt = " + result.getRequestId());
                handler.sendSuccessMessage(request.getObjectKey());
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Logger.e("ErrorCode = " + serviceException.getErrorCode());
                    Logger.e("RequestId = " + serviceException.getRequestId());
                    Logger.e("HostId = " + serviceException.getHostId());
                    Logger.e("RawMessage = " + serviceException.getRawMessage());
                }
                handler.sendFailureMessage(request.getObjectKey(), serviceException);
            }
        });
    }

}
