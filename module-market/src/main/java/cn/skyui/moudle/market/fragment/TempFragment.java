package cn.skyui.moudle.market.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import cn.skyui.library.base.fragment.BaseLazyLoadFragment;
import cn.skyui.moudle.market.R;

/**
 * @author tianshaojie
 * @date 2018/1/27
 */
public class TempFragment extends BaseLazyLoadFragment {

    public static TempFragment newInstance(String title) {
        TempFragment messageFragment = new TempFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        messageFragment.setArguments(bundle);
        return messageFragment;
    }

    int i = 1;
    String title;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_temp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
    }

    public void initData() {
        title = getArguments().getString("title");
    }

    public void initView(View rootView) {
        textView = rootView.findViewById(R.id.textView);
        textView.setText(title);
    }

    @Override
    public void onShow() {
        Logger.i("show - %s", title);
        if(textView != null) {
            textView.postDelayed(() -> textView.setText(title + (i++)), 500);
        }
    }

    @Override
    public void onHide() {
        Logger.i("hide - %s", title);
        if(textView != null) {
            textView.postDelayed(() -> textView.setText("--"), 500);
        }
    }
}

