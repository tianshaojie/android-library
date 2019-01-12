package cn.skyui.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.skyui.library.base.fragment.BaseFragment;

/**
 * @author tianshaojie
 * @date 2018/1/27
 */
public class MessageFragment extends BaseFragment {

    public static Fragment newInstance() {
        return newInstance("消息");
    }

    public static Fragment newInstance(String title) {
        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        messageFragment.setArguments(bundle);
        return messageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}

