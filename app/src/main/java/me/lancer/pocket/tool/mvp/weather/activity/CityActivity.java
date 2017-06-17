package me.lancer.pocket.tool.mvp.weather.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.base.activity.BaseActivity;
import me.lancer.pocket.tool.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.tool.mvp.weather.CityBean;
import me.lancer.pocket.tool.mvp.weather.IWeatherView;
import me.lancer.pocket.tool.mvp.weather.WeatherBean;
import me.lancer.pocket.tool.mvp.weather.WeatherPresenter;
import me.lancer.pocket.tool.mvp.weather.adapter.CityAdapter;
import me.lancer.pocket.tool.mvp.weather.adapter.FutureAdapter;
import me.lancer.pocket.ui.activity.AboutActivity;

public class CityActivity extends PresenterActivity<WeatherPresenter> implements IWeatherView, CityAdapter.MyItemClickListener, CityAdapter.MyItemLongClickListener {

    private Toolbar toolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private CityAdapter mAdapter;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    private List<CityBean> mList = new ArrayList<>();
    private List<String> now = new ArrayList<>();
    private List<String> city = new ArrayList<>();
    private List<String> town = new ArrayList<>();

    private String content;
    private int flag = 0;

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
                    Log.e("log", (String) msg.obj);
                    break;
                case 3:
                    if (msg.obj != null) {
                        now.clear();
                        town.clear();
                        city.clear();
                        mList = (List<CityBean>) msg.obj;
                        for (CityBean bean : mList) {
                            if (!now.contains(bean.getCityName())) {
                                city.add(bean.getCityName());
                                now.add(bean.getCityName());
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case 4:
                    new Thread(loadCity).start();
                    break;
            }
        }
    };

    private Runnable loadFile = new Runnable() {
        @Override
        public void run() {
            AssetManager assetManager = CityActivity.this.getAssets();
            try {
                InputStream is = assetManager.open("citycode.json");
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuffer stringBuffer = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    stringBuffer.append(temp);
                }
                content = stringBuffer.toString();
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable loadCity = new Runnable() {
        @Override
        public void run() {
            presenter.loadCity(content);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        initView();
        initData();
    }

    private void initData() {
        new Thread(loadFile).start();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("城市选择");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_list);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(loadFile).start();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new CityAdapter(this, now);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("搜索...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (now.contains(query)) {
                    Intent intent = new Intent();
                    String id = "CHBJ000000";
                    for (CityBean bean : mList) {
                        if (bean.getTownName().equals(query)) {
                            id = bean.getId();
                        }
                    }
                    intent.putExtra("city", id);
                    intent.setClass(CityActivity.this, WeatherActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                Intent intent0 = new Intent();
                intent0.putExtra("link", "https://github.com/1anc3r");
                intent0.putExtra("title", "Github");
                intent0.setClass(CityActivity.this, AboutActivity.class);
                startActivity(intent0);
                break;
            case R.id.menu_blog:
                Intent intent1 = new Intent();
                intent1.putExtra("link", "https://www.1anc3r.me");
                intent1.putExtra("title", "Blog");
                intent1.setClass(CityActivity.this, AboutActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int postion) {
        if (flag == 1) {
            flag = 0;
            Intent intent = new Intent();
            String id = "CHBJ000000";
            String temp = now.get(postion);
            for (CityBean bean : mList) {
                if (bean.getTownName().equals(temp)) {
                    id = bean.getId();
                }
            }
            intent.putExtra("city", id);
            intent.setClass(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        } else if (flag == 0) {
            flag = 1;
            town.clear();
            String temp = now.get(postion);
            for (CityBean bean : mList) {
                if (bean.getCityName().equals(temp)) {
                    town.add(bean.getTownName());
                }
            }
            now.clear();
            now.addAll(town);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }

    @Override
    protected WeatherPresenter onCreatePresenter() {
        return new WeatherPresenter(this);
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
    public void showWeather(WeatherBean bean) {

    }

    @Override
    public void showCity(List<CityBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }
}
