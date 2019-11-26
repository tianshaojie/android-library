package cn.skyui.library.data.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

import cn.skyui.library.data.constant.Constants;
import cn.skyui.library.http.interceptor.HttpHeaderInterceptor;
import cn.skyui.library.utils.StringUtils;

public class User implements Serializable {

    public String token = "";   // 登录Token
    public long userId = 0;     // 业务用户ID，也是融云IM用户ID
    public boolean isLogin = false;
    public UserVO user = new UserVO();

    private User() {}

    private static class Holder {
        private static final User INSTANT = new User();
    }

    public static User getInstance() {
        return Holder.INSTANT;
    }

    public User init() {
        String response = com.tencent.mmkv.MMKV.defaultMMKV().decodeString(Constants.MMKV.USER, "");
        if(!StringUtils.isEmpty(response)) {
            JSONObject object = JSON.parseObject(response);
            this.token = object.getString("token");
            this.userId = object.getLongValue("userId");
            this.user.setId(userId);
            this.isLogin = true;
            HttpHeaderInterceptor.addHeader("token", this.token);
        }
        return getInstance();
    }

    public void clear() {
        this.token = "";
        this.userId = 0;
        this.isLogin = true;
        this.user = new UserVO();
        HttpHeaderInterceptor.removeHeader("token");
    }
}
