package cn.skyui.library.base.mvp;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V extends IView> implements IPresenter<V> {

    /**
     * 绑定的 view
     */
    private WeakReference<V> mReference;

    /**
     * 绑定 view
     * 调用时机为 view 层的 onCreate()
     * 设置弱引用，防止内存泄漏
     *
     * @param view  绑定的 view
     */
    @Override
    public void attachView(V view) {
        this.mReference = new WeakReference<V>(view);
    }


    /**
     * 解绑 view
     * 调用时机为 view 层的 onDestroy()
     * 将 presenter 层对 view 的引用置空，防止 view 不能被GC，导致内存泄漏
     */
    @Override
    public void detachView() {
        if (mReference != null) {
            mReference.clear();
            mReference = null;
        }
    }

    /**
     * 是否绑定 View
     * @return true 绑定，false 未绑定
     */
    public boolean isAttach() {
        return null != mReference && null != mReference.get();
    }

    /**
     * 获取绑定的 view
     */
    public V getView() {
        return mReference.get();
    }

}
