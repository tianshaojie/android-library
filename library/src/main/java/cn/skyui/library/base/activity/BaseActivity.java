package cn.skyui.library.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import cn.skyui.library.R;
import cn.skyui.library.utils.AppUtils;
import cn.skyui.library.utils.KeyboardUtils;
import cn.skyui.library.utils.imm.IMMLeaks;

/**
 * 处理通用逻辑（二级界面请使用子类侧滑返回BaseSwipeBackActivity）
 * @author tianshaojie
 * @date 2018/1/27
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    // 让5.0之前的系统可以用Vector图标
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public final static int APP_STATUS_KILLED = 0; // 表示应用是被杀死后在启动的
    public final static int APP_STATUS_NORMAL = 1; // 表示应用时正常的启动流程
    public static int APP_STATUS = APP_STATUS_KILLED; // 记录App的启动状态

    protected Activity mActivity;
    protected View toolbar;
    protected TextView mToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 设置主题
        AppCompatDelegate.setDefaultNightMode(AppUtils.getNightMode());;
        super.onCreate(savedInstanceState);
        // 修复系统输入法Bug：在15<=API<=23存在内存泄漏，https://zhuanlan.zhihu.com/p/20828861
        IMMLeaks.fixFocusedViewLeak(getApplication());
        mActivity = this;
        if(APP_STATUS  != APP_STATUS_NORMAL) {
            ARouter.getInstance().build("/app/splash").navigation();
            finish();
            return;
        }
        onCreateSafely(savedInstanceState);
    }

    /**
     * 提供给子Activity设置界面的接口，不要在onCreate中初始化界面
     * @param savedInstanceState
     */
    protected abstract void onCreateSafely(@Nullable Bundle savedInstanceState);

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mActivity = this;
    }

    /**
     * 统一Toolbar：Activity布局内<include layout="@layout/toolbar"/>
     * 如果使用Toolbar，自动开启沉浸状态栏模式
     * 如果布局有toolbar，需要让ImmersionBar接管，否则配合左滑返回，背景不会到顶部
     * 1. 标题
     *  标题居中显示
     *  默认使用AndroidManifest的android:label属性值
     *  可以在onCreate->setContentView之后，调用setToolbarTitle更新Title
     * 2. Icon
     *  app:navigationIcon可以自定义放回按钮，也可代码设置
     * 3. 间距
     *  app:contentInsetStart=0 去掉默认左侧间距
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            if(toolbar instanceof Toolbar) {
                setSupportActionBar((Toolbar) toolbar);
                // 显示返回键
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }

            // 隐藏默认标题
            mToolbarTitle = toolbar.findViewById(R.id.toolbar_title);
            if (mToolbarTitle != null && getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

        if(isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }

    /**
     * 统一Toolbar返回按钮处理
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 覆盖onTitleChanged：让 title 默认显示到 mToolbarTitle，而且不需要暴露 mToolbarTitle 属性
     * 1. 默认显示：AndroidManifest的android:label属性值
     * 2. 代码设置：直接调用Activity.setTitle()方法动态修改
     * @param title
     * @param color
     */
    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        }
    }

    /**
     * 布局没有Toolbar，初始化ImmersionBar
     * 子类可覆盖该方法，设置更多特殊属性，比如透明通栏
     *
     * 3.0版本，不需要在onDestroy方法里调用ImmersionBar的destroy方法
     */
    public void initImmersionBar() {
//        if(toolbar != null) {
//            ImmersionBar.with(this).keyboardEnable(true)
//                    .titleBar(toolbar)
//                    .statusBarColor(R.color.colorPrimary)
//                    .navigationBarColor(R.color.colorPrimary)
//                    .init();
//        } else {
//            ImmersionBar.with(this).keyboardEnable(true)
//                    .statusBarColor(R.color.colorPrimary)
//                    .navigationBarColor(R.color.colorPrimary)
//                    .init();
//        }
    }

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    public boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        KeyboardUtils.hideSoftInput(this);
    }
}