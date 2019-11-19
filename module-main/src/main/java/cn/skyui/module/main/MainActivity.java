package cn.skyui.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import cn.skyui.library.base.activity.BaseActivity;
import cn.skyui.library.utils.StringUtils;
import cn.skyui.library.utils.ToastUtils;
import cn.skyui.library.base.fragment.BaseLazyLoadFragment;
import cn.skyui.module.main.fragment.CustomViewPager;
import cn.skyui.module.main.model.MainIntentProtocol;
import cn.skyui.moudle.market.fragment.TempFragment;
import cn.skyui.moudle.market.fragment.MainQuoteFragment;

/**
 * @author tianshaojie
 * @date 2018/1/15
 */
@Route(path = "/main/main")
public class MainActivity extends BaseActivity {

    private int selectedTabIndex = MainIntentProtocol.DEFAULT_SELECTED_INDEX;
    // 外部跳转到首页的协议类
    private MainIntentProtocol protocol;

    private RadioGroup radioGroup;
    private CustomViewPager fragmentViewPager;
    private List<BaseLazyLoadFragment> fragments = new ArrayList<>();

    @Override
    protected void onCreateSafely(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).init();
        initFragments();
        initView();
        initByIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initByIntent(intent);
    }

    private void initByIntent(Intent intent) {
        if(intent == null) {
            return;
        }
        protocol = getIntent().getParcelableExtra(MainIntentProtocol.MAIN_PROTOCOL);
        if(protocol == null) {
            protocol =  MainIntentProtocol.DEFAULT;
        }
        showSelectedFragment(protocol.primaryTabIndex);
        updateFragmentArguments(protocol.bundle);
        if(StringUtils.isNotEmpty(protocol.openPageRouter)) {
            ARouter.getInstance().build(protocol.openPageRouter)
                    .withBundle(MainIntentProtocol.MAIN_PROTOCOL_BUNDLE, protocol.bundle)
                    .navigation();
        }
    }

    private void initFragments() {
        fragments.clear();
        fragments.add(MainQuoteFragment.newInstance());
        fragments.add(TempFragment.newInstance("交易"));
        fragments.add(TempFragment.newInstance("资讯"));
        fragments.add(TempFragment.newInstance("我的"));
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
        if(index == selectedTabIndex) {
            return;
        }
        fragmentViewPager.post(() -> fragments.get(selectedTabIndex).hide());
        selectedTabIndex = index;
        fragmentViewPager.setCurrentItem(selectedTabIndex);
        fragmentViewPager.post(() -> fragments.get(selectedTabIndex).show());
    }

    private void updateFragmentArguments(Bundle bundle) {
        if(bundle != null) {
            fragments.get(selectedTabIndex).setArguments(bundle);
        }
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