package cn.skyui.moudle.market.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.skyui.library.base.fragment.BaseLazyLoadFragment;
import cn.skyui.library.widgets.tabstrip.PagerSlidingTabStrip;
import cn.skyui.moudle.market.R;

public class MainQuoteFragment extends BaseLazyLoadFragment {

    public static final int DEFAULT_SELECTED_INDEX = 0;
    private int selectedIndex = DEFAULT_SELECTED_INDEX;

    private List<String> mTitleList = Arrays.asList("自选", "市场");
    private List<BaseLazyLoadFragment> fragments = new ArrayList<>();

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip tabStrip;

    public static MainQuoteFragment newInstance() {
        return new MainQuoteFragment();
    }

    @Override
    public int getLayoutId() {
        title = "行情";
        return R.layout.fragment_main_quote;
    }

    @Override
    public void initView(View view) {
        mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_fragment_quote);
        tabStrip = view.findViewById(R.id.tabs);
        mViewPager = view.findViewById(R.id.view_pager);

        initFragments();
        mToolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if(id == R.id.action_search) {
                ARouter.getInstance().build("/market/search").navigation();
            }
            return true;
        });
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

        mViewPager.setAdapter(new QuoteAdapter(getChildFragmentManager()));
        tabStrip.setViewPager(mViewPager);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            selectedIndex = bundle.getInt("selectedIndex", DEFAULT_SELECTED_INDEX);
        }
        mViewPager.setCurrentItem(selectedIndex);
    }

    private void initFragments() {
        fragments.clear();
        fragments.add(TempFragment.newInstance("自选"));
        fragments.add(TempFragment.newInstance("市场"));
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
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}