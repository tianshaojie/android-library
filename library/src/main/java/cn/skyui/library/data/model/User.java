package cn.skyui.library.data.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mmkv.MMKV;

import java.io.Serializable;

import cn.skyui.library.data.constant.Constants;
import cn.skyui.library.http.interceptor.HttpHeaderInterceptor;
import cn.skyui.library.utils.StringUtils;

public class User implements Serializable {

    public String token = "";   // 登录Token
    public String imToken = ""; // 融云 IM Token
    public long userId = 0;     // 业务用户ID，也是融云IM用户ID
    public int status = 1;      // 用户资料完善状态，1：未完善，2：已完善；
    public boolean isLogin = false;
    public UserDetailVO detail = new UserDetailVO();
    public UserLocation location;

    private User() {}

    private static class Holder {
        private static final User INSTANT = new User();
    }

    public static User getInstance() {
        return Holder.INSTANT;
    }

    public User init() {
        String response = MMKV.defaultMMKV().decodeString(Constants.SharedPreferences.USER, "");
        if(!StringUtils.isEmpty(response)) {
            JSONObject object = JSON.parseObject(response);
            this.token = object.getString("token");
            this.imToken = object.getString("imToken");
            this.userId = object.getLongValue("userId");
            this.detail.getUser().setId(userId);
            this.detail.getAccount().setId(userId);
            this.status = object.getIntValue("status");
            this.isLogin = true;
            HttpHeaderInterceptor.addHeader("token", this.token);
        }
        return getInstance();
    }

    public void clear() {
        this.token = "";
        this.imToken = "";
        this.userId = 0;
        this.isLogin = true;
        this.status = 1;
        this.detail = new UserDetailVO();
        this.location = null;
        HttpHeaderInterceptor.removeHeader("token");
    }

    public void updateStatus(int status) {
        this.status = status;
        String response = MMKV.defaultMMKV().decodeString(Constants.SharedPreferences.USER, "");
        if(!StringUtils.isEmpty(response)) {
            JSONObject object = JSON.parseObject(response);
            object.put("status", status);
            MMKV.defaultMMKV().encode(Constants.SharedPreferences.USER, object.toJSONString());
        }
    }
}
