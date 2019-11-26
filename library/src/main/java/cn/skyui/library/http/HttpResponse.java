package cn.skyui.library.http;

import java.io.Serializable;

public class HttpResponse<T> implements Serializable {

    private int code;
    private String message;
    private String status;
    private T info;

    public boolean isSuccess() {
        return code == 200 || code == 0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
