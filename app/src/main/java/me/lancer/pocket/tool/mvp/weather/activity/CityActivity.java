package me.lancer.pocket.tool.mvp.weather.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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
import me.lancer.pocket.tool.mvp.weather.CityBean;
import me.lancer.pocket.tool.mvp.weather.IWeatherView;
import me.lancer.pocket.tool.mvp.weather.WeatherBean;
import me.lancer.pocket.tool.mvp.weather.WeatherPresenter;
import me.lancer.pocket.tool.mvp.weather.adapter.CityAdapter;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;

public class CityActivity extends PresenterActivity<WeatherPresenter> implements IWeatherView, CityAdapter.MyItemClickListener, CityAdapter.MyItemLongClickListener {

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private CityAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;

    private List<CityBean> list = new ArrayList<>();
    private List<String> now = new ArrayList<>();
    private List<String> city = new ArrayList<>();
    private List<String> town = new ArrayList<>();

    private String content;
    private int flag = 0;
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
                        now.clear();
                        town.clear();
                        city.clear();
                        list = (List<CityBean>) msg.obj;
                        for (CityBean bean : list) {
                            if (!now.contains(bean.getCityName())) {
                                city.add(bean.getCityName());
                                now.add(bean.getCityName());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 4:
                    new Thread(loadCity).start();
                    break;
            }
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CityActivity.this, WeatherActivity.class);
                startActivity(intent);
                finish();
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.srl);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(loadFile).start();
            }
        });
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        adapter = new CityAdapter(this, now);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        rvList.setAdapter(adapter);
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
                    for (CityBean bean : list) {
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
                showAboutDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("天气");
        builder.setMessage(
                "\t\t\t\t/*\n" +
                        "\t\t\t\t * 天气信息 : 提供全国各城市的天气信息\n" +
                        "\t\t\t\t * 城市选择 : 然而并不能定位\n" +
                        "\t\t\t\t * ——数据来源 : 中央天气\n" +
                        "\t\t\t\t * （tj.nineton.cn/Heart/index）\n" +
                        "\t\t\t\t */");
        builder.show();
    }

    @Override
    public void onItemClick(View view, int postion) {
        if (flag == 1) {
            flag = 0;
            Intent intent = new Intent();
            String id = "CHBJ000000";
            String temp = now.get(postion);
            for (CityBean bean : list) {
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
            for (CityBean bean : list) {
                if (bean.getCityName().equals(temp)) {
                    town.add(bean.getTownName());
                }
            }
            now.clear();
            now.addAll(town);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClass(CityActivity.this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
