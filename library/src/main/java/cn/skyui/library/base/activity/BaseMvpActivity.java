package cn.skyui.library.base.activity;

import android.os.Bundle;

import cn.skyui.library.base.mvp.BasePresenter;
import cn.skyui.library.base.mvp.IView;

public abstract class BaseMvpActivity<V extends IView, P extends BasePresenter<V>>
        extends BaseActivity
        implements IView {

    private P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
        initData();
        initView();
    }

    /**
     * 抽象方法，每个布局设置布局文件
     */
    public abstract int getContentViewId();

    /**
     * 抽象方法，创建对应的Presenter，只有子类及同一个包类可以访问
     */
    protected abstract P createPresenter();

    // 初始化方法
    public abstract void initData();

    // 初始化方法
    public abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

}
