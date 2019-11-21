package cn.skyui.moudle.market.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.skyui.library.base.fragment.BaseLazyLoadFragment;
import cn.skyui.moudle.market.R;

/**
 * @author tianshaojie
 * @date 2018/1/27
 */
public class NoToolbarTempFragment extends BaseLazyLoadFragment {

    public static NoToolbarTempFragment newInstance(String title) {
        NoToolbarTempFragment fragment = new NoToolbarTempFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    int i = 1;
    TextView textView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_temp_no_toolbar;
    }

    @Override
    public void initView(View view) {
        textView = view.findViewById(R.id.textView);
        textView.setText(title);
        textView.setText("--");
    }

    @Override
    public void initData() {
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

