package me.lancer.pocket.info.mvp.code.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.base.activity.PresenterActivity;
import me.lancer.pocket.info.mvp.code.CodeBean;
import me.lancer.pocket.info.mvp.code.CodePresenter;
import me.lancer.pocket.info.mvp.code.ICodeView;
import me.lancer.pocket.info.mvp.code.adapter.CodeAdapter;

public class CodeSearchActivity extends PresenterActivity<CodePresenter> implements ICodeView {

    Toolbar toolbar;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private CodeAdapter mAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private List<CodeBean> mList = new ArrayList<>();

    private String keyword;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 1:
                    mSwipeRefreshLayout.setRefreshing(true);
                    break;
                case 2:
                    break;
                case 3:
                    if (msg.obj != null) {
                        mList = (List<CodeBean>) msg.obj;
                        mAdapter = new CodeAdapter(CodeSearchActivity.this, mList);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    private Runnable loadQuery = new Runnable() {
        @Override
        public void run() {
            presenter.loadSearching(keyword);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initData();
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("搜索结果");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_result);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(loadQuery).start();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_result);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CodeAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        keyword = getIntent().getStringExtra("query");
        if (keyword != null) {
            new Thread(loadQuery).start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("搜索...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keyword = query;
                new Thread(loadQuery).start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                showAboutDialog();
                break;
        }
        return true;
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编程");
        builder.setMessage(
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 个人 : GitHub上Star最多的个人\n" +
                "\t\t\t\t * 组织 : GitHub上Star最多的组织\n" +
                "\t\t\t\t * 项目 : GitHub上Star最多的项目\n" +
                "\t\t\t\t * 趋势 : GitHub上今日最热的项目\n" +
                "\t\t\t\t * 搜索 : 点击右上角的搜索按钮\n" +
                "\t\t\t\t *        搜索你想浏览的项目\n" +
                "\t\t\t\t * ——数据来源 : GithubRanking\n\t\t\t\t（github-ranking.com）\n" +
                "\t\t\t\t */");
        builder.show();
    }

    @Override
    protected CodePresenter onCreatePresenter() {
        return new CodePresenter(this);
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
    public void showUsers(List<CodeBean> list) {

    }

    @Override
    public void showOrganizations(List<CodeBean> list) {

    }

    @Override
    public void showRepositories(List<CodeBean> list) {

    }

    @Override
    public void showTrending(List<CodeBean> list) {

    }

    @Override
    public void showSearching(List<CodeBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showDetail(CodeBean bean) {

    }
}
