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
public class MarketTabFragment extends BaseLazyLoadFragment {

    private static final String SELECTED_INDEX = "selectedIndex";
    public static final int DEFAULT_SELECTED_INDEX = 0;
    private int selectedIndex = DEFAULT_SELECTED_INDEX;

    private static final String[] TITLES = {"智能", "全景", "沪深", "港通"};
    private PagerSlidingTabStrip tabs;
    private ViewPager mViewPager;


    public static MarketTabFragment newInstance(String title) {
        MarketTabFragment fragment = new MarketTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_INDEX, selectedIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_market_tab;
    }

    @Override
    public void initView(View view) {
        tabs = view.findViewById(R.id.tabs);
        mViewPager = view.findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(TITLES.length);
        mViewPager.setAdapter(new MarketPagerAdapter(getChildFragmentManager()));
        tabs.setViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                selectedIndex = position;
            }
        });
    }

    @Override
    public void onFirstShow() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            selectedIndex = bundle.getInt(SELECTED_INDEX, DEFAULT_SELECTED_INDEX);
        }
        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX, DEFAULT_SELECTED_INDEX);
        }
        mViewPager.setCurrentItem(selectedIndex);
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
                    TempListFragment.newInstance("智能"),
                    TempListFragment.newInstance("全景"),
                    TempListFragment.newInstance("沪深"),
                    TempListFragment.newInstance("港通")
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
