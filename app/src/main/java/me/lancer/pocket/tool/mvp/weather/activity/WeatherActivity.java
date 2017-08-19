package me.lancer.pocket.tool.mvp.weather.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.weather.CityBean;
import me.lancer.pocket.tool.mvp.weather.FutureBean;
import me.lancer.pocket.tool.mvp.weather.IWeatherView;
import me.lancer.pocket.tool.mvp.weather.WeatherBean;
import me.lancer.pocket.tool.mvp.weather.WeatherPresenter;
import me.lancer.pocket.tool.mvp.weather.adapter.FutureAdapter;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;

public class WeatherActivity extends PresenterActivity<WeatherPresenter> implements IWeatherView {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private CollapsingToolbarLayout layout;
    private FloatingActionButton fab;
    private ImageView ivImg, ivWeather;
    private TextView tvTemperature, tvAirQuality,
            tvDressingBrief, tvDressingDetail,
            tvUvBrief, tvUvDetail,
            tvCarWashingBrief, tvCarWashingDetail,
            tvTravelBrief, tvTravelDetail,
            tvFluBrief, tvFluDetail,
            tvSportBrief, tvSportDetail;
    private RecyclerView rvList;
    private FutureAdapter adapter;
    private LinearLayoutManager layoutManager;
    private WeatherBean mBean = new WeatherBean();
    private List<FutureBean> list = new ArrayList<>();
    private String city = "CHBJ000000";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    if (msg.obj != null) {
                        mBean = (WeatherBean) msg.obj;
                        layout.setTitle(mBean.getCityName());
                        SimpleDateFormat formatter = new SimpleDateFormat("HH");
                        int date = Integer.parseInt(formatter.format(new Date(System.currentTimeMillis())));
                        if (date > Integer.parseInt(mBean.getSunrise().split(":")[0]) && date < (12 + Integer.parseInt(mBean.getSunrise().split(":")[0]))) {
                            Glide.with(WeatherActivity.this).load("https://raw.githubusercontent.com/1anc3r/Pocket/master/ic_day.png").into(ivImg);
                        } else {
                            Glide.with(WeatherActivity.this).load("https://raw.githubusercontent.com/1anc3r/Pocket/master/ic_night.png").into(ivImg);
                        }
                        String weather = mBean.getWeather();
                        if (weather.contains("阴") || weather.contains("云")) {
                            ivWeather.setImageResource(R.mipmap.ic_cloudy);
                        } else if (weather.contains("晴")) {
                            ivWeather.setImageResource(R.mipmap.ic_sunny);
                        } else if (weather.contains("雨")) {
                            ivWeather.setImageResource(R.mipmap.ic_rainy);
                        } else if (weather.contains("雪")) {
                            ivWeather.setImageResource(R.mipmap.ic_snowy);
                        }
                        tvTemperature.setText(weather + "    " + mBean.getTemperature() + "℃");
                        tvAirQuality.setText("PM2.5 : " + mBean.getPm25() + "    空气质量 : " + mBean.getAirQuality());
                        tvDressingBrief.setText("穿衣指数 — " + mBean.getDressingBrief());
                        tvDressingDetail.setText(mBean.getDressingDetails());
                        tvUvBrief.setText("紫外线指数 — " + mBean.getUvBrief());
                        tvUvDetail.setText(mBean.getUvDetails());
                        tvCarWashingBrief.setText("洗车指数 — " + mBean.getCarWashingBrief());
                        tvCarWashingDetail.setText(mBean.getCarWashingDetails());
                        tvTravelBrief.setText("旅行指数 — " + mBean.getTravelBrief());
                        tvTravelDetail.setText(mBean.getTravelDetails());
                        tvFluBrief.setText("流感指数 — " + mBean.getFluBrief());
                        tvFluDetail.setText(mBean.getFluDetails());
                        tvSportBrief.setText("运动指数 — " + mBean.getSportBrief());
                        tvSportDetail.setText(mBean.getSportDetails());
                        list.clear();
                        list.addAll(mBean.getFuture());
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    private Runnable loadWeather = new Runnable() {
        @Override
        public void run() {
            presenter.loadWeather(city);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initData();
        initView();
    }

    private void initData() {
        sharedPreferences = getSharedPreferences(getString(R.string.spf_user), Context.MODE_PRIVATE);
        if (getIntent().getStringExtra("city") != null) {
            city = getIntent().getStringExtra("city");
            editor = sharedPreferences.edit();
            editor.putString("city", city);
            editor.apply();
        } else {
            city = sharedPreferences.getString("city", "CHBJ000000");
        }
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.t_weather);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        layout = (CollapsingToolbarLayout) findViewById(R.id.ctl_weather);
        layout.setTitle("天气");
        fab = (FloatingActionButton) findViewById(R.id.fab_collect);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(WeatherActivity.this, CityActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ivImg = (ImageView) findViewById(R.id.iv_cover);
        ivWeather = (ImageView) findViewById(R.id.iv_weather);
        tvTemperature = (TextView) findViewById(R.id.tv_temperature);
        tvAirQuality = (TextView) findViewById(R.id.tv_air_quality);
        tvDressingBrief = (TextView) findViewById(R.id.tv_dressing_brief);
        tvDressingDetail = (TextView) findViewById(R.id.tv_dressing_detail);
        tvUvBrief = (TextView) findViewById(R.id.tv_uv_brief);
        tvUvDetail = (TextView) findViewById(R.id.tv_uv_detail);
        tvCarWashingBrief = (TextView) findViewById(R.id.tv_car_washing_brief);
        tvCarWashingDetail = (TextView) findViewById(R.id.tv_car_washing_detail);
        tvTravelBrief = (TextView) findViewById(R.id.tv_travel_brief);
        tvTravelDetail = (TextView) findViewById(R.id.tv_travel_detail);
        tvFluBrief = (TextView) findViewById(R.id.tv_flu_brief);
        tvFluDetail = (TextView) findViewById(R.id.tv_flu_detail);
        tvSportBrief = (TextView) findViewById(R.id.tv_sport_brief);
        tvSportDetail = (TextView) findViewById(R.id.tv_sport_detail);
        rvList = (RecyclerView) findViewById(R.id.rv_future);
        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        adapter = new FutureAdapter(this, list);
        rvList.setAdapter(adapter);
        new Thread(loadWeather).start();
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
        Message msg = new Message();
        msg.what = 3;
        msg.obj = bean;
        handler.sendMessage(msg);
    }

    @Override
    public void showCity(List<CityBean> list) {

    }
}
