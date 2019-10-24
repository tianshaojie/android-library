package cn.skyui.library.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.skyui.library.base.mvp.BasePresenter;
import cn.skyui.library.base.mvp.IView;

public abstract class BaseMvpFragment<V extends IView, P extends BasePresenter<V>>
        extends BaseFragment
        implements IView {

    private P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    /**
     * 抽象方法，创建对应的Presenter，只有子类及同一个包类可以访问
     */
    protected abstract P createPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
