package cn.skyui.library.http;

import java.io.Serializable;

public class HttpResponse<T> implements Serializable {

    private int code;
    private String msg;
    private T body;

    public HttpResponse() {
    }

    public HttpResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return code == 200;
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

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", body=" + body +
                '}';
    }
}
