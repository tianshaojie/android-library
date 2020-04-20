package cn.skyui.app.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Arrays;

import cn.skyui.app.R;
import cn.skyui.library.base.fragment.BaseFragment;

public class SubListFragment extends BaseFragment {

    public static SubListFragment newInstance() {
        return new SubListFragment();
    }

    private View rootView;
    private RecyclerView mRecyclerView;
//    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BaseQuickAdapter mAdapter;

    private String[] mStrings = {"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return rootView = inflater.inflate(R.layout.fragment_list_demo, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadData();
    }

    private void initView() {
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
//        mSwipeRefreshLayout = rootView.findViewById(R.id.swipeLayout);
//        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.fragment_list_item_demo, null) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.text_content, item);
            }
        };

        mAdapter.setOnLoadMoreListener(() -> mAdapter.loadMoreEnd(), mRecyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        mRecyclerView.setAdapter(mAdapter);
//        mSwipeRefreshLayout.setOnRefreshListener(() -> {
//            loadData();
//        });
//        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void loadData() {
        mAdapter.setNewData(Arrays.asList(mStrings));
        mAdapter.loadMoreComplete();
//        mSwipeRefreshLayout.setRefreshing(false);
    }
}
