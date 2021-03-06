package me.lancer.pocket.info.mvp.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.app.AppBean;
import me.lancer.pocket.info.mvp.app.AppPresenter;
import me.lancer.pocket.info.mvp.app.IAppView;
import me.lancer.pocket.info.mvp.app.adapter.AppGridAdapter;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;

public class AppSearchActivity extends PresenterActivity<AppPresenter> implements IAppView {

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private AppGridAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private List<AppBean> list = new ArrayList<>();
    private int pager = 1, last = 0, load = 0;
    private String query;

    private Runnable loadSearch = new Runnable() {
        @Override
        public void run() {
            presenter.loadSearch(query, pager);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    swipeRefresh.setRefreshing(false);
                    break;
                case 1:
                    swipeRefresh.setRefreshing(true);
                    break;
                case 2:
                    break;
                case 3:
                    if (msg.obj != null) {
                        list = (List<AppBean>) msg.obj;
                        adapter = new AppGridAdapter(AppSearchActivity.this, list);
                        rvList.setAdapter(adapter);
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
                case 4:
                    if (msg.obj != null && load == 1) {
                        int size = list.size();
                        list.addAll((List<AppBean>) msg.obj);
                        for (int i = 0; i < ((List<AppBean>) msg.obj).size(); i++) {
                            adapter.notifyItemInserted(size + i);
                        }
                        load = 0;
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
                case 5:
                    if (msg.obj != null) {
                        list.addAll((List<AppBean>) msg.obj);
                        Collections.sort(list, AppComparator);
                        adapter = new AppGridAdapter(AppSearchActivity.this, list);
                        rvList.setAdapter(adapter);
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        }
    };

    Comparator AppComparator = new Comparator() {
        public int compare(Object obj1, Object obj2) {
            AppBean app1 = (AppBean) obj1;
            AppBean app2 = (AppBean) obj2;
            if (app1.getApkName().compareToIgnoreCase(app2.getApkName()) < 0)
                return -1;
            else if (app1.getApkName().compareToIgnoreCase(app2.getApkName()) == 0)
                return 0;
            else if (app1.getApkName().compareToIgnoreCase(app2.getApkName()) > 0)
                return 1;
            return 0;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initData();
        initView();
    }

    private void initData() {
        query = getIntent().getStringExtra("query");
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("搜索结果");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.srl);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessageDelayed(msg, 800);
            }
        });
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        adapter = new AppGridAdapter(this, list);
        adapter.setHasStableIds(true);
        rvList.setAdapter(adapter);
        rvList.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && last + 1 == adapter.getItemCount()) {
                    load = 1;
                    pager += 1;
                    new Thread(loadSearch).start();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                last = getMax(layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]));
            }
        });
        new Thread(loadSearch).start();
    }

    private int getMax(int[] arr) {
        int len = arr.length;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < len; i++) {
            max = Math.max(max, arr[i]);
        }
        return max;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected AppPresenter onCreatePresenter() {
        return new AppPresenter(this);
    }

    @Override
    public void showMsg(String log) {
        Message msg = new Message();
        msg.what = 2;
        msg.obj = log;
        handler.sendMessage(msg);
    }

    @Override
    public void showLoad() {
        Message msg = new Message();
        msg.what = 1;
        handler.sendMessage(msg);
    }

    @Override
    public void hideLoad() {
        Message msg = new Message();
        msg.what = 0;
        handler.sendMessage(msg);
    }

    @Override
    public void showHomepage(List<AppBean> list) {

    }

    @Override
    public void showSearch(List<AppBean> list) {
        Message msg = new Message();
        if (pager == 1) {
            msg.what = 3;
        } else {
            msg.what = 4;
        }
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showDetail(AppBean bean) {

    }

    @Override
    public void showDownload(String log) {

    }

    @Override
    public void showUpgrade(List<AppBean> list) {

    }
}
