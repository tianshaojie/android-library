package cn.skyui.app;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chenenyu.router.annotation.Route;
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur;
import com.gyf.barlibrary.ImmersionBar;
import com.orhanobut.logger.Logger;

import cn.skyui.library.base.activity.BaseActivity;
import cn.skyui.library.utils.ToastUtils;
import eightbitlab.com.blurview.BlurView;

/**
 * @author tianshaojie
 * @date 2018/1/15
 */
public class MainActivityOri extends BaseActivity {

    private static final String TAB_INDEX_KEY = "tabIndex";
    private static final String[] FRAGMENT_TAGS = new String[]{
            "HomeFragment", "FavoriteFragment", "MessageFragment", "MemberFragment"
    };

    private int tabIndex = 0;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ori);
        ImmersionBar.with(this).init();
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            tabIndex = savedInstanceState.getInt(TAB_INDEX_KEY);
        }
        initView();
    }

    private void initView() {
        BlurView blurViewBottom = findViewById(R.id.blurView);
        final Drawable background = blurViewBottom.getBackground();
        blurViewBottom.setupWith((ViewGroup) blurViewBottom.getParent())
                .windowBackground(background)
                .blurAlgorithm(new SupportRenderScriptBlur(this))
                .blurRadius(20f);

        RadioGroup group = findViewById(R.id.group);

        // 点击效果
        for (int i = 0; i < group.getChildCount(); i++) {
            if (group.getChildAt(i) instanceof RadioButton) {
                UiUtil.addBottomBarClickEffect(group.getChildAt(i));
            }
        }

        // 点击切换
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index;
                switch (checkedId) {
                    case R.id.bottom_bar_home:
                        index = 0;
                        break;
                    case R.id.bottom_bar_favorite:
                        index = 1;
                        break;
                    case R.id.bottom_bar_message:
                        index = 2;
                        break;
                    case R.id.bottom_bar_member:
                        index = 3;
                        break;
                    default:
                        index = 0;
                        break;
                }
                showFragment(index);
            }
        });

        // 显示(首页Tab或者被恢复Tab)
        showFragment(tabIndex);
    }

    private void showFragment(int index) {
        // 隐藏旧Tab
        if (tabIndex != index) {
            Fragment prevFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAGS[tabIndex]);
            if (prevFragment != null) {
                fragmentManager.beginTransaction().hide(prevFragment).commit();
            }
        }
        // 显示新Tab
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAGS[index]);
        if (fragment != null && fragment.isAdded()) {
            fragmentManager.beginTransaction().show(fragment).commit();
        } else {
            fragment = instantFragment(index);
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment, FRAGMENT_TAGS[index]).commit();
        }
        tabIndex = index;
    }

    private Fragment instantFragment(int currIndex) {
        switch (currIndex) {
            case 0:
                return HomeFragment.newInstance("首页");
            case 1:
                return MessageFragment.newInstance("关注");
            case 2:
                return MessageFragment.newInstance("消息");
            case 3:
                return MessageFragment.newInstance("我的");
            default:
                return HomeFragment.newInstance("首页");
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

    @Override
    protected void onStart() {
        super.onStart();
        Logger.i("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i("onResume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(TAB_INDEX_KEY, tabIndex);
        super.onSaveInstanceState(outState);
        Logger.i("onSaveInstanceState");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.i("onPause: isFinish=" + isFinishing());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.i("onStop: isFinish=" + isFinishing());
    }

    @Override
    public void finish() {
        super.finish();
        Logger.i("finish: isFinish=" + isFinishing());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.i("onDestroy: isFinish=" + isFinishing());
    }
}