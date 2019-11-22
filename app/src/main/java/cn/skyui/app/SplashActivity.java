package cn.skyui.app;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import cn.skyui.library.base.activity.BaseActivity;
import cn.skyui.module.main.model.MainIntentProtocol;

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
//        ImmersionBar.with(this).transparentBar().init();
        enter();
    }

    private void enter() {
        MainIntentProtocol protocol = new MainIntentProtocol(0);
        ARouter.getInstance().build("/main/main")
                .withParcelable(MainIntentProtocol.MAIN_PROTOCOL, protocol)
                .navigation();
        finish();
    }
}