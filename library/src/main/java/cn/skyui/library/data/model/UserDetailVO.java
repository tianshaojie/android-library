package cn.skyui.library.data.model;

import java.io.Serializable;

/**
 * Created by tiansj on 2018/4/8.
 */

public class UserDetailVO implements Serializable {

    private UserVO user = new UserVO();
    private UserAccountVO account = new UserAccountVO();

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public UserAccountVO getAccount() {
        return account;
    }

    public void setAccount(UserAccountVO account) {
        this.account = account;
    }
}
