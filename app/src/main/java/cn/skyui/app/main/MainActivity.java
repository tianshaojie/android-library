package cn.skyui.app.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import cn.skyui.app.R;
import cn.skyui.app.main.fragment.MainHomeFragment;
import cn.skyui.app.main.fragment.MainMineFragment;
import cn.skyui.app.main.fragment.TempFragment;
import cn.skyui.library.base.activity.BaseActivity;
import cn.skyui.library.utils.ToastUtils;

/**
 * @author tianshaojie
 * @date 2018/1/15
 */
@Route(path = "/app/main")
public class MainActivity extends BaseActivity {

    public static final String SELECTED_INDEX = "selectedIndex";
    private static final int DEFAULT_SELECTED_INDEX = 0;

    private int selectedFragmentIndex = DEFAULT_SELECTED_INDEX;

    private RadioGroup radioGroup;
    private CustomViewPager fragmentViewPager;
    private List<BaseLazyLoadFragment> fragments = new ArrayList<>();

    @Override
    protected void onCreateSafely(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        initFragments();
        initView();
        showSelectedFragment(getIntent().getIntExtra(SELECTED_INDEX, DEFAULT_SELECTED_INDEX));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showSelectedFragment(intent.getIntExtra(SELECTED_INDEX, DEFAULT_SELECTED_INDEX));
    }

    private void initFragments() {
        fragments.clear();
        fragments.add(MainHomeFragment.newInstance("首页"));
        fragments.add(TempFragment.newInstance("关注"));
        fragments.add(TempFragment.newInstance("消息"));
        fragments.add(MainMineFragment.newInstance("我的"));
    }

    private void initView() {
        fragmentViewPager = findViewById(R.id.fragment_container);
        fragmentViewPager.setOffscreenPageLimit(fragments.size());
        fragmentViewPager.setCanScroll(false);
        fragmentViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        fragmentViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                radioGroup.check(radioGroup.getChildAt(position).getId());
            }
        });

        radioGroup = findViewById(R.id.group);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            final int index = i;
            radioGroup.getChildAt(i).setOnClickListener(v -> showSelectedFragment(index));
        }
    }

    private void showSelectedFragment(int index) {
        if(index < 0 || index >= fragments.size()) {
            index = 0;
        }
        if(index != selectedFragmentIndex) {
            fragments.get(selectedFragmentIndex).hide();
        }
        selectedFragmentIndex = index;
        fragmentViewPager.setCurrentItem(selectedFragmentIndex);
        fragmentViewPager.post(() -> fragments.get(selectedFragmentIndex).show());
    }

    private long firstTime = 0;
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            ToastUtils.showShort("再按一次退出程序");
            firstTime = secondTime;
        } else {
            finish();
        }
    }

}