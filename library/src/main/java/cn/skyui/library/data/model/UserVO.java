package cn.skyui.library.data.model;

import java.io.Serializable;

/**
 * Created by tiansj on 2018/4/8.
 */

public class UserVO  implements Serializable {

    private Long id = 0L;
    private String phone;
    private String nickname = "";
    private String avatar = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
