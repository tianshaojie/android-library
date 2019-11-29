package cn.skyui.moudle.market.fragment;

import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.skyui.library.base.fragment.BaseLazyLoadFragment;
import cn.skyui.moudle.market.R;

/**
 * @author tianshaojie
 * @date 2018/1/27
 */
public class TempFragment extends BaseLazyLoadFragment {

    public static TempFragment newInstance(String title) {
        TempFragment fragment = new TempFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    int i = 1;
    TextView textView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_temp;
    }

    @Override
    public void initView(View view) {
        textView = view.findViewById(R.id.textView);
        textView.setText(title);
        textView.setText("--");
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if("Temp".equals(title) && toolbar != null) {
            ActionMenuView actionMenuView = toolbar.findViewById(R.id.action_menu_view);
            mActivity.getMenuInflater().inflate(R.menu.menu_back, actionMenuView.getMenu());
            actionMenuView.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_back) {
                    FrameLayout frameLayout = mActivity.findViewById(R.id.market_fragment_container);
                    if(frameLayout != null) {
                        frameLayout.setVisibility(View.GONE);
                        mActivity.getSupportFragmentManager().beginTransaction().remove(TempFragment.this).commit();
                    }
                }
                return true;
            });
        }
    }

    @Override
    public void onFirstShow() {
        textView.postDelayed(() -> textView.setText(title), 1000);
    }

    @Override
    public void onShow() {
        if(textView != null) {
            textView.postDelayed(() -> textView.setText(title + (i++)), 1000);
        }
    }

    @Override
    public void onHide() {
        if(textView != null) {
            textView.postDelayed(() -> textView.setText("--"), 1000);
        }
    }
}

