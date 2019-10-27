package cn.skyui.library.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxFragment;

import cn.skyui.library.R;

/**
 * @author tianshaojie
 * @date 2018/1/31
 */
public class BaseFragment extends RxFragment {

    // 让5.0之前的系统可以用Vector图标
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected AppCompatActivity mActivity;
//    protected ImmersionBar mImmersionBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (isImmersionBarEnabled()) {
//            View toolbar = view.findViewById(R.id.toolbar);
//            if(toolbar != null) {
//                TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
//                if(toolbarTitle != null && getArguments() != null && getArguments().getString("title") != null) {
//                    toolbarTitle.setText(getArguments().getString("title"));
//                }
//                ImmersionBar.setTitleBar(mActivity, toolbar);
//            }
//            initImmersionBar();
//        }
    }

    /**
     * 是否在Fragment使用沉浸式，子类可覆盖
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始化沉浸式，子类可覆盖
     */
    protected void initImmersionBar() {
//        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mImmersionBar != null) {
//            mImmersionBar.destroy();
//        }
    }
}
