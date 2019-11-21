package cn.skyui.library.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author tianshaojie
 *
 * 懒加载Fragment基类：支持ViewPager多级嵌套
 *
 * @see #getLayoutId()  当前界面的资源文件Id
 * @see #initView(View) 初始化控件，只做findViewById工作不加载数据
 * @see #initData()     Fragment首次可以见调用，只调用一次，用来做初始化或加载数据工作；
 * @see #onShow()       Fragment变为可见时回调
 * @see #onHide()       Fragment变为隐藏时回调
 *
 */
public abstract class BaseLazyLoadFragment extends BaseFragment {
    
    private static final String TAG = "BaseLazyLoadFragment";

    protected String title;
    protected View rootView;
    
    private boolean isViewCreated = false;

    /**
     * 是否首次显示，目的是为了初始化方法只调用一次；
     */
    private boolean isFirstVisible = false;

    /**
     * 自身显示状态
     */
    private boolean isVisibleToUser = false;

    /**
     * 针对多级Fragment嵌套时，子Fragment持有父Fragment的显示状态
     * 1. 在自身setUserVisibleHint触发时，isVisible与isParentVisible状态一致；
     * 2. 在父Fragment调用showChildFragment，hideChildFragment，更新子Fragment的isParentVisible。
     */
    private boolean isParentVisible = false;
    

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            title = getArguments().getString("title", "--");
        }
    }

    /**
     * 同一Activity下多个Fragment切换时会重复执行onCreateView方法
     * 解决办法是在onCreateView方法中缓存rootView
     *
     * @param inflater              布局加载器
     * @param container             父类控件
     * @param savedInstanceState    上次保存的数据
     * @return 返回加载的视图
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            initView(rootView);
            isViewCreated = true;
        } else {
            if (rootView.getParent() != null) {
                ((ViewGroup) rootView.getParent()).removeView(rootView);
            }
        }
        return rootView;
    }

    /**
     * 获得当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    public abstract int getLayoutId();

    /**
     * 初始化控件，只做findViewById工作不加载数据
     * @param view Fragment对应的布局根View
     */
    public abstract void initView(View view);

    /**
     * 通过getUserVisibleHint判断Fragment是显示状态，再调用onFirstUserVisible
     * 针对ViewPager内的Fragment，只加载第一个Fragment
     * @param savedInstanceState 保存的状态
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.e(TAG, "onActivityCreated isVisibleToUser = " + isVisibleToUser  + ", isParentVisible = " + isParentVisible + ", title = " + title);
        if (isVisibleToUser && !isFirstVisible && isParentVisibleToUser()) {
            onFirstUserVisible();
        }
    }

    private boolean isParentVisibleToUser() {
        Fragment fragment = getParentFragment();
        if(fragment == null) {
            // 如果没有父级的Fragment，说明父级直接是Activity，设置为true
            return true;
        }
        else if(fragment instanceof BaseLazyLoadFragment) {
            return ((BaseLazyLoadFragment)fragment).isVisibleToUser;
        } else {
            return false;
        }
    }

    /**
     * 对于单个 Fragment，setUserVisibleHint 是不会被调用的，只有该 Fragment 在 ViewPager 里才会被调用。
     *
     * 进行双重标记判断
     * 是因为setUserVisibleHint会多次回调，并且会在onCreateView执行前回调
     * 必须确保onCreateView加载完毕且页面可见才加载数据
     *
     * 在ViewPager内切换时，会调用FragmentPagerAdapter.setPrimaryItem，进而调用Fragment的setUserVisibleHint方法。
     * 但ViewPager内切换，只会调用Adapter的直接Fragment，不会调用Fragment的子Fragment，比如Fragment内还有ViewPager+Fragment的结构；
     * 所有需要再 setUserVisibleHint 调用 show、hide时，再调用showChildFragment，hideChildFragment；
     *
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getArguments() != null) {
            title = getArguments().getString("title", "--");
        }
        // 自己的显示状态
        this.isVisibleToUser = isVisibleToUser;
        // setUserVisibleHint方法调用时，自己显示Parent也是显示状态
        this.isParentVisible = isVisibleToUser;
//        Log.e(TAG, "setUserVisibleHint = " + title + ", isVisibleToUser = " + isVisibleToUser);
        if(!isViewCreated) {
            return;
        }
        if (isVisibleToUser) {
            if (!isFirstVisible) {
                onFirstUserVisible();
            } else {
                show();
            }
        } else {
            // 已经调用过onFirstUserVisible再调用hide，防止没有显示过还去调hide
            if (isFirstVisible) {
                hide();
            }
        }
    }

    /**
     * 设置自身显示
     */
    private void show() {
        Log.e(TAG, "show = " + title);
        // 显示自己
        onShow();
        // 显示子Fragment
        showChildFragment(getChildFragmentManager());
    }

    /**
     * 设置自身隐藏
     */
    private void hide() {
        Log.e(TAG, "hide = " + title);
        // 隐藏自己
        onHide();
        // 隐藏子Fragment
        hideChildFragment(getChildFragmentManager());
    }

    /**
     * 父Fragment被显示，同时调用显示子Fragment，只针对之前显示状态的子Fragment处理
     */
    private void showChildFragment(FragmentManager fragmentManager) {
        if(fragmentManager == null) {
            return;
        }
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments.isEmpty()) {
            return;
        }
        for (Fragment fragment : fragments) {
            if(fragment instanceof BaseLazyLoadFragment) {
                BaseLazyLoadFragment childFragment = (BaseLazyLoadFragment) fragment;
                // 通知子Fragment父级已经显示
                childFragment.isParentVisible = true;
                // 只通知之前是显示状态的子Fragment
                if(childFragment.isVisibleToUser) {
                    childFragment.onShow();
                    showChildFragment(childFragment.getChildFragmentManager());
                    Log.e(TAG, "showChildFragment = " + childFragment.title);
                }
            }
        }
    }



    /**
     * 父Fragment被隐藏，同时调用隐藏子Fragment，只针对之前显示状态的子Fragment处理
     */
    private void hideChildFragment(FragmentManager fragmentManager) {
        if(fragmentManager == null) {
            return;
        }
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments.isEmpty()) {
            return;
        }
        for (Fragment fragment : fragments) {
            if(fragment instanceof BaseLazyLoadFragment) {
                BaseLazyLoadFragment childFragment = (BaseLazyLoadFragment) fragment;
                // 通知子Fragment父级已经隐藏
                childFragment.isParentVisible = false;
                // 只通知之前是显示状态的子Fragment
                if(childFragment.isVisibleToUser) {
                    childFragment.onHide();
                    hideChildFragment(childFragment.getChildFragmentManager());
                    Log.e(TAG, "hideChildFragment = " + childFragment.title);
                }
            }
        }
    }

    /**
     * Fragment首次可以见调用（onActivityCreated和setUserVisibleHint触发）
     */
    protected void onFirstUserVisible() {
        isFirstVisible = true;
        isVisibleToUser = true;
        isParentVisible = true;
        initData();
        Log.e(TAG, "onFirstUserVisible = " + title);
        // 针对二级Fragment，非首个tab下的ViewPager下的Fragment(三级)
        initChildFragmentOnFirstUserVisible(getChildFragmentManager());
    }

    /**
     * 父Fragment被显示，同时调用显示子Fragment，只针对之前显示状态的子Fragment处理
     */
    private void initChildFragmentOnFirstUserVisible(FragmentManager fragmentManager) {
        if(fragmentManager == null) {
            return;
        }
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments.isEmpty()) {
            return;
        }
        for (Fragment fragment : fragments) {
            if(fragment instanceof BaseLazyLoadFragment) {
                BaseLazyLoadFragment childFragment = (BaseLazyLoadFragment) fragment;
                // 只通知之前是显示状态的子Fragment
                if(childFragment.isVisibleToUser && !childFragment.isFirstVisible) {
                    Log.e(TAG, "initChildFragmentOnFirstUserVisible = " + childFragment.title);
                    childFragment.initData();
                    initChildFragmentOnFirstUserVisible(childFragment.getChildFragmentManager());
                }
            }
        }
    }

    /**
     * 此方法只调用一次，用来做初始化或加载数据工作；
     */
    public abstract void initData();


    /**
     * 避免onResume和onActivityCreated同时调用了onFirstUserVisible和onShow方法
     */
    private boolean isFirstResume = true;

    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     *
     * 所有Fragment都会触发onResume，如果当前父级Fragment是可见状态，即isParentVisible=true，再调用自己的onShow
     * (父级Fragment没有显示，子级Fragment肯定不是可见状态，不再调用onShow)
     */
    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (isFragmentVisible()) {
            onShow();
            Log.e(TAG, "onResume onShow = " + title);
        }
    }

    /**
     * 所有Fragment都会触发onPause，如果当前父级Fragment是可见状态，即isParentVisible=true，再调用自己的onHide
     * (父级Fragment没有显示，子级Fragment肯定不是可见状态，说明onHide之前已经调用过)
     */
    @Override
    public void onPause() {
        super.onPause();
        if (isFragmentVisible()) {
            onHide();
            Log.e(TAG, "onPause onHide = " + title);
        }
    }

    /**
     * 判断当前Fragment是否为可见状态
     * 自己可见isVisibleToUser && 父级Fragment可见 isParentVisible
     *
     * @return true 可见，false 不可见
     */
    private boolean isFragmentVisible() {
        return isVisibleToUser && isParentVisibleToUser();
    }

    /**
     * Fragment变为可见时回调
     */
    public abstract void onShow();

    /**
     * Fragment变为隐藏时回调
     */
    public abstract void onHide();


}
