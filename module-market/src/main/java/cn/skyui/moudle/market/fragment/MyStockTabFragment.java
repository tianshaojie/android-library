package cn.skyui.moudle.market.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import cn.skyui.library.base.fragment.BaseFragment;
import cn.skyui.library.base.fragment.BaseLazyLoadFragment;
import cn.skyui.library.widgets.tabstrip.PagerSlidingTabStrip;
import cn.skyui.moudle.market.R;

/**
 * @author tianshaojie
 * @date 2018/12/4
 * @desc:
 */
public class MyStockTabFragment extends BaseLazyLoadFragment {

    public static final String SELECTED_TAB_INDEX = "selectedTabIndex";
    public static final int DEFAULT_SELECTED_INDEX = 0;
    private int selectedTabIndex = DEFAULT_SELECTED_INDEX;

    private static final String[] TITLES = {"全部", "沪深", "港股", "美股"};
    private PagerSlidingTabStrip tabs;
    private ViewPager mViewPager;


    public static MyStockTabFragment newInstance(String title) {
        MyStockTabFragment fragment = new MyStockTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_quote_tab;
    }

    @Override
    public void initView(View view) {
        tabs = view.findViewById(R.id.tabs);
        mViewPager = view.findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(TITLES.length);
        mViewPager.setAdapter(new MarketPagerAdapter(getChildFragmentManager()));
        tabs.setViewPager(mViewPager);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            selectedTabIndex = bundle.getInt(SELECTED_TAB_INDEX, DEFAULT_SELECTED_INDEX);
        }
        mViewPager.setCurrentItem(selectedTabIndex);
    }

    @Override
    public void onShow() {
    }

    @Override
    public void onHide() {
    }

    public class MarketPagerAdapter extends FragmentPagerAdapter {

        private BaseFragment[] fragments;

        MarketPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new BaseFragment[] {
                    TempListFragment.newInstance("全部"),
                    TempListFragment.newInstance("沪深"),
                    TempListFragment.newInstance("港股"),
                    TempListFragment.newInstance("美股")
            };
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

    }

}
