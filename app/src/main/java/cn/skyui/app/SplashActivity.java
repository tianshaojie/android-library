package cn.skyui.app;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;

import cn.skyui.library.base.activity.BaseActivity;
import cn.skyui.module.main.MainActivity;

/**
 * @author tianshaojie
 * @date 2018/1/15
 */
@Route(path = "/app/splash")
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BaseActivity.APP_STATUS = BaseActivity.APP_STATUS_NORMAL;
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).transparentBar().init();
        enter();
    }

    private void enter() {
        ARouter.getInstance().build("/main/main").withInt(MainActivity.SELECTED_INDEX, MainActivity.DEFAULT_SELECTED_INDEX).navigation();
        finish();
    }
}