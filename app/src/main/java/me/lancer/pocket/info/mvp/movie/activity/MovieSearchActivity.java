package me.lancer.pocket.info.mvp.movie.activity;

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
import me.lancer.pocket.info.mvp.movie.IMovieView;
import me.lancer.pocket.info.mvp.movie.MovieBean;
import me.lancer.pocket.info.mvp.movie.MoviePresenter;
import me.lancer.pocket.info.mvp.movie.adapter.MovieAdapter;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;

public class MovieSearchActivity extends PresenterActivity<MoviePresenter> implements IMovieView {

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private MovieAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<MovieBean> list = new ArrayList<>();

    private String keyword;

    private Runnable loadQuery = new Runnable() {
        @Override
        public void run() {
            presenter.loadTopMovie(keyword);
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
                        list = (List<MovieBean>) msg.obj;
                        adapter = new MovieAdapter(MovieSearchActivity.this, list);
                        rvList.setAdapter(adapter);
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
        initView();
        initData();
    }

    private void initData() {
        keyword = getIntent().getStringExtra("query");
        if (keyword != null) {
            new Thread(loadQuery).start();
        }
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
                new Thread(loadQuery).start();
            }
        });
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        adapter = new MovieAdapter(this, list);
        rvList.setAdapter(adapter);
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
        builder.setTitle("电影");
        builder.setMessage(
                "\t\t\t\t/*\n" +
                        "\t\t\t\t * 影评 : 豆瓣电影的最受欢迎影评\n" +
                        "\t\t\t\t * 影榜 : 爬取呈现豆瓣电影TOP250\n" +
                        "\t\t\t\t * 搜索 : 点击右上角的搜索按钮\n" +
                        "\t\t\t\t *        搜索你想了解的电影信息\n" +
                        "\t\t\t\t * ——数据来源 : 豆瓣电影\n" +
                        "\t\t\t\t * （movie.douban.com）\n" +
                        "\t\t\t\t */");
        builder.show();
    }

    @Override
    protected MoviePresenter onCreatePresenter() {
        return new MoviePresenter(this);
    }


    @Override
    public void showReviewer(List<MovieBean> list) {

    }

    @Override
    public void showTopMovie(List<MovieBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showReviewerDetail(MovieBean bean) {

    }

    @Override
    public void showTopDetail(MovieBean bean) {

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
