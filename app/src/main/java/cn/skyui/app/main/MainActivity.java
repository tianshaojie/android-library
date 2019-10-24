package cn.skyui.app.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.chenenyu.router.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import cn.skyui.app.MyStockFragment;
import cn.skyui.app.R;
import cn.skyui.library.base.activity.BaseActivity;
import cn.skyui.library.utils.ToastUtils;

/**
 * @author tianshaojie
 * @date 2018/1/15
 */
@Route("main")
public class MainActivity extends BaseActivity {

    public static final String SELECTED_INDEX = "selectedIndex";
    private static final int DEFAULT_SELECTED_INDEX = 0;

    private int selectedFragmentIndex = DEFAULT_SELECTED_INDEX;

    private RadioGroup radioGroup;
    private CustomViewPager fragmentViewPager;
    private List<BaseLazyLoadFragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        initView();
        selectedFragmentIndex = getIntent().getIntExtra(SELECTED_INDEX, DEFAULT_SELECTED_INDEX);
        showSelectedFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        selectedFragmentIndex = intent.getIntExtra(SELECTED_INDEX, DEFAULT_SELECTED_INDEX);
        showSelectedFragment();
    }

    private void initFragment() {
        fragments.add(MyStockFragment.newInstance("首页"));
        fragments.add(MyStockFragment.newInstance("关注"));
        fragments.add(MyStockFragment.newInstance("消息"));
        fragments.add(MyStockFragment.newInstance("我的"));
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
                BaseLazyLoadFragment hideFragment = fragments.get(selectedFragmentIndex);
                if(hideFragment != null) {
                    hideFragment.hide();
                    radioGroup.getChildAt(selectedFragmentIndex).setSelected(false);
                }
                BaseLazyLoadFragment showFragment = fragments.get(position);
                if(showFragment != null) {
                    showFragment.show();
                    radioGroup.getChildAt(position).setSelected(true);
                }
                selectedFragmentIndex = position;
            }
        });

        radioGroup = findViewById(R.id.group);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            fragmentViewPager.setCurrentItem(group.indexOfChild(group.findViewById(checkedId)));
        });
    }

    private void showSelectedFragment() {
        for(int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(selectedFragmentIndex).setSelected(false);
        }
        radioGroup.getChildAt(selectedFragmentIndex).setSelected(true);
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