package cn.skyui.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import cn.skyui.library.base.activity.BaseActivity;
import cn.skyui.library.utils.AppUtils;
import cn.skyui.module.main.model.MainIntentProtocol;

/**
 * @author tianshaojie
 * @date 2018/1/15
 */
@Route(path = "/app/splash")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BaseActivity.APP_STATUS = BaseActivity.APP_STATUS_NORMAL;
        AppCompatDelegate.setDefaultNightMode(AppUtils.getNightMode());;
        super.onCreate(savedInstanceState);
        enter();
    }

    private void enter() {
        MainIntentProtocol protocol = new MainIntentProtocol(0);
        ARouter.getInstance().build("/main/main")
                .withParcelable(MainIntentProtocol.MAIN_PROTOCOL, protocol)
                .navigation(this);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}