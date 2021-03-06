package cn.skyui.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gyf.immersionbar.ImmersionBar;

import cn.skyui.app.main.MainActivity;
import cn.skyui.library.base.activity.BaseActivity;

/**
 * @author tianshaojie
 * @date 2018/1/15
 */
@Route(path = "/app/splash")
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BaseActivity.APP_STATUS = BaseActivity.APP_STATUS_NORMAL;
//        setTheme(R.style.SplashThemeHuawei);
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).transparentBar().init();
//        getWindow().getDecorView().postDelayed(this::enter, 100);
        enter();
    }

    private void enter() {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.SELECTED_INDEX, -1);
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        getWindow().getDecorView().getHandler().removeCallbacksAndMessages(null);
    }
}