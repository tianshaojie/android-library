package cn.skyui.moudle.market.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.Arrays;
import java.util.List;

import cn.skyui.library.base.fragment.BaseLazyLoadFragment;
import cn.skyui.library.widget.tabstrip.PagerSlidingTabStrip;
import cn.skyui.moudle.market.R;

public class MainQuoteFragment extends BaseLazyLoadFragment {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip tabStrip;

    public static BaseLazyLoadFragment newInstance() {
        return new MainQuoteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_quote, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_fragment_quote);
        tabStrip = view.findViewById(R.id.tabs);
        mViewPager = view.findViewById(R.id.view_pager);
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.action_search) {
                    ARouter.getInstance().build("/market/search").navigation();
                }
//                else if(id == R.id.action_refresh) {
//                    ARouter.getInstance().build(FZUrlRouterManagerBase.kFZUrlRouterKeyForProblemCollection)
//                            .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            .navigation();
//                }
                return false;
            }
        });
        mViewPager.setAdapter(new QuoteAdapter(getChildFragmentManager()));
        tabStrip.setViewPager(mViewPager);
    }

    class QuoteAdapter extends FragmentPagerAdapter {

        private List<String> mTitleList = Arrays.asList("自选", "市场");
        public QuoteAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return TempListFragment.newInstance();
//                return MyStockTabFragment.newInstance();
            } else if (position == 1) {
//                return new MarketTabFragment();
                return MarketTabFragment.newInstance();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }

        @Override
        public int getCount() {
            return mTitleList.size();
        }
    }

}