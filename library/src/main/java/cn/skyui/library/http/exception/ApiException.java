package cn.skyui.library.http.exception;

import android.util.MalformedJsonException;

import com.alibaba.fastjson.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * Created by tianshaojie on 2017/11/8.
 */

public class ApiException extends Exception {

    private static final int CONNECT_ERROR_CODE = -1;
    private static final String NETWORK_ERROR_MSG = "网络异常，请检查网络";
    private static final String HTTP_ERROR_MSG = "网络错误，请重试";

    private static final int TIMEOUT_ERROR_CODE = -2;
    private static final String TIMEOUT_ERROR_MSG = "连接超时，请稍后再试";

    private static final int UNKNOWN_ERROR_CODE = -3;
    private static final String UNKNOWN_ERROR_MSG = "未知错误，请重试";

    private static final int PARSE_ERROR_CODE = -4;
    private static final String PARSE_ERROR_MSG = "解析服务器响应数据失败";

    private int code;
    private String msg;

    private ApiException(Throwable throwable, int code, String msg) {
        super(throwable);
        this.code = code;
        this.msg = msg;
    }

    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ApiException handleException(Throwable e) {
        if (e instanceof HttpException) {             //We had non-2XX http error
            HttpException httpException = (HttpException) e;
            return new ApiException(e, httpException.code(), HTTP_ERROR_MSG); //均视为网络错误
        } else if (e instanceof JSONException
                || e instanceof ParseException
                || e instanceof MalformedJsonException) {  //解析数据错误
            return new ApiException(e, PARSE_ERROR_CODE, PARSE_ERROR_MSG);
        } else if (e instanceof ConnectException) {//连接网络错误
            return  new ApiException(e, CONNECT_ERROR_CODE, NETWORK_ERROR_MSG);
        } else if (e instanceof SocketTimeoutException) {//网络超时
            return  new ApiException(e, TIMEOUT_ERROR_CODE, TIMEOUT_ERROR_MSG);
        } else {  //未知错误
            return  new ApiException(e, UNKNOWN_ERROR_CODE, UNKNOWN_ERROR_MSG);
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
