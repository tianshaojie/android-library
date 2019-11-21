package cn.skyui.library.base.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxFragment;

import cn.skyui.library.R;

/**
 * 为了方便在Fragment使用沉浸式请继承ImmersionFragment，
 * 请在immersionBarEnabled方法中实现你的沉浸式代码，
 * ImmersionProxy已经做了ImmersionBar.with(mFragment).destroy()了，所以不需要在你的代码中做这个处理了
 * 如果不能继承，请拷贝代码到你的项目中
 *
 * 注意：
 * 不要再Fragment的跟布局使用系统的fitsSystemWindows属性
 *
 * @author tianshaojie
 * @date 2018/1/31
 */
public abstract class BaseFragment extends RxFragment {

    // 让5.0之前的系统可以用Vector图标
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected AppCompatActivity mActivity;
    protected View toolbar;
    /**
     * Fragment的view是否已经初始化完成
     */
    private boolean mIsActivityCreated;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        if(toolbar != null) {
            TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
            if(toolbarTitle != null && getArguments() != null && getArguments().getString("title") != null) {
                toolbarTitle.setText(getArguments().getString("title"));
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsActivityCreated = true;
        if (immersionBarEnabled()) {
            initImmersionBar();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mIsActivityCreated && getUserVisibleHint() && immersionBarEnabled()) {
            initImmersionBar();
        }
    }

    public void initImmersionBar() {
        if(toolbar != null) {
            ImmersionBar.with(this).keyboardEnable(true).titleBar(toolbar).statusBarColor(R.color.colorPrimary).init();
        } else {
            ImmersionBar.with(this).keyboardEnable(true).statusBarColor(R.color.colorPrimary).init();
        }
    }

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    public boolean immersionBarEnabled() {
        return true;
    }

}
