package cn.skyui.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
import com.gyf.barlibrary.ImmersionBar;

import cn.skyui.app.main.MainActivity;
import cn.skyui.library.base.activity.BaseActivity;

/**
 * @author tianshaojie
 * @date 2018/1/15
 */
@Route("splash")
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
        Intent intent = new Intent();
        intent.putExtra(MainActivity.SELECTED_INDEX, -4);
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().getDecorView().getHandler().removeCallbacksAndMessages(null);
        ImmersionBar.with(this).destroy();
    }
}