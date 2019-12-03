package cn.skyui.moudle.market.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Arrays;

import cn.skyui.library.base.fragment.BaseLazyLoadFragment;
import cn.skyui.library.web.activity.WebViewActivity;
import cn.skyui.moudle.market.R;

public class TempListFragment extends BaseLazyLoadFragment {

    public static TempListFragment newInstance(String title) {
        TempListFragment fragment = new TempListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BaseQuickAdapter mAdapter;

    private String[] mStrings = {"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler"};

    @Override
    public int getLayoutId() {
        return R.layout.fragment_list_demo;
    }

    @Override
    public void initView(View view) {
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.fragment_list_item_demo, null) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.text_content, item);
            }
        };
        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
//            FrameLayout frameLayout = mActivity.findViewById(R.id.market_fragment_container);
//            if(frameLayout != null) {
//                frameLayout.setVisibility(View.VISIBLE);
//                mActivity.getSupportFragmentManager().beginTransaction().add(R.id.market_fragment_container, TempFragment.newInstance("Temp")).commit();
//            }
            showWebViewActivity(mActivity, "http://skyui.cn/interest/h5.html");
        });

        mAdapter.setOnLoadMoreListener(() -> mAdapter.loadMoreEnd(), mRecyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            loadData();
        });
    }

    public static void showWebViewActivity(Activity activity, String url) {
//        Intent intent = new Intent();
//        intent.putExtra("url", url);
//        intent.setClass(activity, WebViewActivity.class);
//        activity.startActivity(intent);

        ARouter.getInstance().build("/web/h5")
                .withString("url", url)
                .navigation(activity);
    }

    @Override
    public void onFirstShow() {
        mSwipeRefreshLayout.setRefreshing(true);
        loadData();
    }

    private void loadData() {
        mRecyclerView.postDelayed(() -> {
            mAdapter.setNewData(Arrays.asList(mStrings));
            mAdapter.loadMoreComplete();
            mSwipeRefreshLayout.setRefreshing(false);
        }, 1500);
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }
}
