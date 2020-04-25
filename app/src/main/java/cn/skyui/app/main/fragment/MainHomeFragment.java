package cn.skyui.app.main.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import cn.skyui.app.R;
import cn.skyui.app.main.BaseLazyLoadFragment;

public class MainHomeFragment extends BaseLazyLoadFragment {

    public static MainHomeFragment newInstance(String title) {
        MainHomeFragment mineFragment = new MainHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        mineFragment.setArguments(bundle);
        return mineFragment;
    }

    private AppBarLayout appBar;

    /**
     * 大布局背景，遮罩层
     */
    private View bgContent;
    /**
     * 展开状态下toolbar显示的内容
     */
    private View toolbarOpen;
    /**
     * 展开状态下toolbar的遮罩层
     */
    private View bgToolbarOpen;
    /**
     * 收缩状态下toolbar显示的内容
     */
    private View toolbarClose;
    /**
     * 收缩状态下toolbar的遮罩层
     */
    private View bgToolbarClose;

    private SlidingTabLayout tabLayout1;
    private SlidingTabLayout tabLayout2;
    private ViewPager fragmentViewPager;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragments.add(SubListFragment.newInstance());
        fragments.add(SubWebViewFragment.newInstance());

        appBar = view.findViewById(R.id.app_bar);
        bgContent = view.findViewById(R.id.bg_content);
        toolbarOpen = view.findViewById(R.id.include_toolbar_open);
        bgToolbarOpen = view.findViewById(R.id.bg_toolbar_open);
        toolbarClose = view.findViewById(R.id.include_toolbar_close);
        bgToolbarClose = view.findViewById(R.id.bg_toolbar_close);

        tabLayout1 = view.findViewById(R.id.slidingTabLayout1);
        tabLayout2 = view.findViewById(R.id.slidingTabLayout2);


        fragmentViewPager = view.findViewById(R.id.viewPager);
        fragmentViewPager.setOffscreenPageLimit(fragments.size());
        fragmentViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        tabLayout1.setViewPager(fragmentViewPager, new String[] {"首页", "智能"});
        tabLayout2.setViewPager(fragmentViewPager, new String[] {"首页", "智能"});

        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            //垂直方向偏移量
            int offset = Math.abs(verticalOffset);
            //最大偏移距离
            int scrollRange = appBarLayout.getTotalScrollRange();
            if (offset <= scrollRange / 2) {
                //当滑动没超过一半，展开状态下toolbar显示内容，根据收缩位置，改变透明值
                toolbarOpen.setVisibility(View.VISIBLE);
                toolbarClose.setVisibility(View.GONE);
                //根据偏移百分比 计算透明值
                float scale2 = (float) offset / (scrollRange / 2);
                int alpha2 = (int) (255 * scale2);
                bgToolbarOpen.setBackgroundColor(Color.argb(alpha2, 25, 131, 209));
            } else {
                //当滑动超过一半，收缩状态下toolbar显示内容，根据收缩位置，改变透明值
                toolbarClose.setVisibility(View.VISIBLE);
                toolbarOpen.setVisibility(View.GONE);
                float scale3 = (float) (scrollRange  - offset) / (scrollRange / 2);
                int alpha3 = (int) (255 * scale3);
                bgToolbarClose.setBackgroundColor(Color.argb(alpha3, 25, 131, 209));
            }
            float scale = (float) offset / scrollRange;
            int alpha = (int) (255 * scale);
            bgContent.setBackgroundColor(Color.argb(alpha, 25, 131, 209));
        });

    }

    @Override
    public void onShow() {
        ImmersionBar.with(this)
                .titleBar(mActivity.findViewById(R.id.toolbarHome))
                .init();
    }

    @Override
    public void onHide() {
    }
}