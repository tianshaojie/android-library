package cn.skyui.library.base.mvp;

public interface IPresenter<V extends IView> {

    /**
     * 绑定 view
     * @param view 绑定的 view
     */
    void attachView(V view);


    /**
     * 解绑 view
     */
    void detachView();
}
