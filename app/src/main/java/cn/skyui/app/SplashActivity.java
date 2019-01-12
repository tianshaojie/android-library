package cn.skyui.app;

import android.app.Activity;
import android.os.Bundle;

import com.chenenyu.router.Router;
import com.gyf.barlibrary.ImmersionBar;

import cn.skyui.app.library.base.activity.BaseActivity;

/**
 * @author tianshaojie
 * @date 2018/1/15
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BaseActivity.APP_STATUS = BaseActivity.APP_STATUS_NORMAL;
//        setTheme(R.style.SplashThemeHuawei);
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).transparentBar().init();
        getWindow().getDecorView().postDelayed(this::enter, 100);


    }

    private void enter() {
        Router.build("main").go(this);
//        if(User.getInstance().isLogin) {
//            Router.build("main").go(this);
//        } else {
//            Router.build("login").go(this);
//        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().getDecorView().getHandler().removeCallbacksAndMessages(null);
        ImmersionBar.with(this).destroy();
    }
}