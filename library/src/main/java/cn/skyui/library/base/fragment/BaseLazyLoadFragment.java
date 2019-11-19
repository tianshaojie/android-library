package cn.skyui.library.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import cn.skyui.library.base.fragment.BaseFragment;

public abstract class BaseLazyLoadFragment extends BaseFragment {

    private static final String TAG = "BaseLazyLoadFragment";
    private boolean isVisible = false;
    private boolean isViewInitiated = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewInitiated = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible) {
            onShow();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isVisible) {
            onHide();
        }
    }

    public void show() {
        if(isViewInitiated) {
            isVisible = true;
            Log.i(TAG, "execute onShow()");
            onShow();
        }
    }

    public void hide() {
        if(isViewInitiated) {
            isVisible = false;
            Log.i(TAG, "execute onHide()");
            onHide();
        }
    }

    /**
     * 开始刷新数据
     */
    public abstract void onShow();

    /**
     * 停止刷新数据
     */
    public abstract void onHide();
}
