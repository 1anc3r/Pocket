package me.lancer.pocket.info.mvp.novel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.novel.INovelView;
import me.lancer.pocket.info.mvp.novel.NovelBean;
import me.lancer.pocket.info.mvp.novel.NovelPresenter;
import me.lancer.pocket.info.mvp.novel.adapter.NovelImgAdapter;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;

/**
 * Created by HuangFangzhi on 2017/5/25.
 */

public class NovelListActivity extends PresenterActivity<NovelPresenter> implements INovelView {

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private NovelImgAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private List<NovelBean> list = new ArrayList<>();

    private int type, value3, value4, last = 0;
    private String value1, value2;

    private Runnable loadRank = new Runnable() {
        @Override
        public void run() {
            presenter.loadRank(value1);
        }
    };

    private Runnable loadCate = new Runnable() {
        @Override
        public void run() {
            presenter.loadCate(value1, value2, value3, value4);
        }
    };

    private Runnable loadSearch = new Runnable() {
        @Override
        public void run() {
            presenter.loadSearch(value1);
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
                        list.addAll((List<NovelBean>) msg.obj);
                        adapter.notifyDataSetChanged();
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
            }
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
        type = getIntent().getIntExtra("type", 1);
        value1 = getIntent().getStringExtra("value1");
        value2 = getIntent().getStringExtra("value2");
        value3 = getIntent().getIntExtra("value3", 0);
        value4 = getIntent().getIntExtra("value4", 50);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (type == 1) {
                actionBar.setTitle(value2);
            } else if (type == 2) {
                actionBar.setTitle(value2);
            } else if (type == 3) {
                actionBar.setTitle("搜索结果");
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.srl);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load(type);
            }
        });
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        adapter = new NovelImgAdapter(this, list);
        rvList.setAdapter(adapter);

        rvList.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && last + 1 == adapter.getItemCount() && type == 2) {
                    value3 += 10;
                    new Thread(loadCate).start();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                last = getMax(layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]));
            }
        });
        load(type);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_normal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                showAboutDialog();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("小说");
        builder.setMessage(
                "\t\t\t\t/*\n" +
                        "\t\t\t\t * 排行 : 小说排行榜\n" +
                        "\t\t\t\t * 分类 : 小说各分区\n" +
                        "\t\t\t\t * 搜索 : 点击右上角的搜索按钮搜索你想看的小说\n" +
                        "\t\t\t\t * ——数据来源 : 追书神器\n" +
                        "\t\t\t\t * （www.zhuishushenqi.com）\n" +
                        "\t\t\t\t */");
        builder.show();
    }

    private void load(int method) {
        switch (method) {
            case 1:
                new Thread(loadRank).start();
                break;
            case 2:
                new Thread(loadCate).start();
                break;
            case 3:
                new Thread(loadSearch).start();
                break;
        }
    }

    public static void startActivity(Activity activity, int type, String value1, String value2, int value3, int value4) {
        Intent intent = new Intent();
        intent.setClass(activity, NovelListActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("value1", value1);
        intent.putExtra("value2", value2);
        intent.putExtra("value3", value3);
        intent.putExtra("value4", value4);
        ActivityCompat.startActivity(activity, intent, new Bundle());
    }

    @Override
    protected NovelPresenter onCreatePresenter() {
        return new NovelPresenter(this);
    }

    @Override
    public void showRank(List<NovelBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showCate(List<NovelBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showSearch(List<NovelBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showDetail(NovelBean bean) {

    }

    @Override
    public void showChapter(List<NovelBean.Chapters> list) {

    }

    @Override
    public void showContent(String content) {

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
}
