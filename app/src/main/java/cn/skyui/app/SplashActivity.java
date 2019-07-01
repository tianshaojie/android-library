package cn.skyui.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
import com.gyf.barlibrary.ImmersionBar;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;

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
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).transparentBar().init();

        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                new CheckRequestPermissionListener() {
                    @Override
                    public void onPermissionOk(Permission permission) {
                        enter();
                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {
                        enter();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void enter() {
        Router.build("main").go(this);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}