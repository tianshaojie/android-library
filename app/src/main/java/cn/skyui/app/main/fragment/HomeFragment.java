package cn.skyui.app.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import cn.skyui.app.R;
import cn.skyui.app.main.BaseLazyLoadFragment;

public class HomeFragment extends BaseLazyLoadFragment {

    public static HomeFragment newInstance(String title) {
        HomeFragment mineFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        mineFragment.setArguments(bundle);
        return mineFragment;
    }

    private SlidingTabLayout tabLayout;
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
        fragments.add(SubListFragment.newInstance());
        fragments.add(SubListFragment.newInstance());

        tabLayout = view.findViewById(R.id.slidingTabLayout);
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
        tabLayout.setViewPager(fragmentViewPager, new String[] {"Tab1", "Tab2", "Tab3"});
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {
    }
}