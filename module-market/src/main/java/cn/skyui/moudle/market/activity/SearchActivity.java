package cn.skyui.moudle.market.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.skyui.library.base.activity.BaseSwipeBackActivity;
import cn.skyui.library.data.constant.Constants;
import cn.skyui.library.http.HttpObserver;
import cn.skyui.library.http.HttpService;
import cn.skyui.library.http.RxSchedulers;
import cn.skyui.library.http.exception.ApiException;
import cn.skyui.moudle.market.R;
import cn.skyui.moudle.market.data.ApiService;
import cn.skyui.moudle.market.data.model.SearchResult;
import cn.skyui.moudle.market.data.model.SearchResultItem;

@Route(path = "/market/search")
public class SearchActivity extends BaseSwipeBackActivity {

    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;
    private TextView mTextEmptyTips;

    private String keyword = "";

    @Override
    protected void onCreateSafely(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        mTextEmptyTips = findViewById(R.id.textEmptyTips);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mAdapter = new BaseQuickAdapter<SearchResultItem, BaseViewHolder>(R.layout.activity_stock_search_item, null) {
            @Override
            protected void convert(BaseViewHolder helper, SearchResultItem item) {
                helper.setText(R.id.stock_name, item.name);
                helper.setText(R.id.stock_code, item.code);
            }
        };
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
//        mAdapter.setOnLoadMoreListener(this::loadData, mRecyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stock_search, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) mSearch.getActionView();
        searchView.setQueryHint("证券代码/名称/首字母/拼音");
        searchView.onActionViewExpanded();

        LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(16);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query != null && query.length() > 0) {
                    keyword = query;
                    mAdapter.getData().clear();
                    loadData();
                }
                mRecyclerView.setVisibility(View.VISIBLE);
                mTextEmptyTips.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText == null || newText.length() == 0) {
                    mAdapter.getData().clear();
                    mAdapter.notifyDataSetChanged();
                    mTextEmptyTips.setVisibility(View.GONE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mTextEmptyTips.setVisibility(View.GONE);
                    keyword = newText;
                    loadData();
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void loadData() {
        HttpService.createService(ApiService.class)
                .search(keyword, Constants.DEFAULT_PAGE_SIZE)
                .compose(RxSchedulers.io2main())
                .subscribe(new HttpObserver<SearchResult>() {
                    @Override
                    protected void onSuccess(SearchResult result) {
                        List<SearchResultItem> list = result.stocks;
                        if(list.isEmpty()) {
                            mTextEmptyTips.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        } else {
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mTextEmptyTips.setVisibility(View.GONE);
                        }
                        mAdapter.setNewData(list);
                        mAdapter.loadMoreEnd(true);
                    }

                    @Override
                    protected void onFailure(ApiException e) {
                        super.onFailure(e);
                        mAdapter.loadMoreFail();
                    }
                });
    }

}
