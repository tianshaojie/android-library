package cn.skyui.moudle.market.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.skyui.library.base.fragment.BaseLazyLoadFragment;
import cn.skyui.library.utils.AppUtils;
import cn.skyui.library.widgets.tabstrip.PagerSlidingTabStrip;
import cn.skyui.moudle.market.R;

public class MainMarketFragment extends BaseLazyLoadFragment {

    private static final String SELECTED_INDEX = "selectedIndex";
    public static final int DEFAULT_SELECTED_INDEX = 0;
    private int selectedIndex = DEFAULT_SELECTED_INDEX;

    private List<String> mTitleList = Arrays.asList("自选", "市场");
    private List<BaseLazyLoadFragment> fragments;

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip tabStrip;
    private FrameLayout frameLayout;

    public static MainMarketFragment newInstance(String title) {
        MainMarketFragment fragment = new MainMarketFragment();
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
        return R.layout.fragment_main_market;
    }

    @Override
    public void initView(View view) {
        mToolbar = view.findViewById(R.id.toolbar);
        tabStrip = view.findViewById(R.id.tabs);
        mViewPager = view.findViewById(R.id.view_pager);
        frameLayout = view.findViewById(R.id.market_fragment_container);

        ActionMenuView actionMenuView = mToolbar.findViewById(R.id.action_menu_view);
        mActivity.getMenuInflater().inflate(R.menu.menu_fragment_market_main_left, actionMenuView.getMenu());
        actionMenuView.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_skin) {
                AppUtils.changeNightMode(mActivity, !AppUtils.isNightMode());
            }
            return true;
        });
        mToolbar.inflateMenu(R.menu.menu_fragment_market_main_right);
        mToolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_search) {
                ARouter.getInstance().build("/market/search").navigation();
            }
            return true;
        });

        initFragments();
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                selectedIndex = position;
            }
        });

        mViewPager.setAdapter(new QuoteAdapter(getChildFragmentManager()));
        tabStrip.setViewPager(mViewPager);
    }

    @Override
    public void onFirstShow() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            selectedIndex = bundle.getInt("selectedIndex", DEFAULT_SELECTED_INDEX);
        }
        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX, DEFAULT_SELECTED_INDEX);
        }
        mViewPager.setCurrentItem(selectedIndex);
    }

    private void initFragments() {
        fragments = new ArrayList<>(mTitleList.size());
        for (int i = 0; i < mTitleList.size(); i++) {
            fragments.add(null);
        }
    }

    @Override
    public void onShow() {
    }

    @Override
    public void onHide() {
    }


    class QuoteAdapter extends FragmentPagerAdapter {

        QuoteAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MyStockTabFragment.newInstance(mTitleList.get(position));
                case 1:
                    return MarketTabFragment.newInstance(mTitleList.get(position));
                default:
                    return null;
            }
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            BaseLazyLoadFragment object = (BaseLazyLoadFragment) super.instantiateItem(container, position);
            fragments.set(position, object);
            return object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.destroyItem(container, position, object);
            fragments.set(position, null);
        }

        @Override
        public int getCount() {
            return mTitleList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }

}