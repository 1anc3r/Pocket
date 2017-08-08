package me.lancer.pocket.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.weather.CityBean;
import me.lancer.pocket.tool.mvp.weather.IWeatherView;
import me.lancer.pocket.tool.mvp.weather.WeatherBean;
import me.lancer.pocket.tool.mvp.weather.WeatherPresenter;
import me.lancer.pocket.ui.base.fragment.BaseFragment;
import me.lancer.pocket.ui.activity.SettingActivity;
import me.lancer.pocket.ui.base.fragment.PresenterFragment;
import me.lancer.pocket.ui.toy.DragonActivity;

import static android.content.Intent.ACTION_VIEW;

public class MainFragment extends PresenterFragment<WeatherPresenter> implements IWeatherView {

    private Toolbar toolbar;
    private int index = 0;
    private WeatherBean mBean = new WeatherBean();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
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
                        String weather = mBean.getWeather();
                        if (weather.contains("阴") || weather.contains("云")) {
                            toolbar.setLogo(R.mipmap.ic_cloudy);
                        } else if (weather.contains("晴")) {
                            toolbar.setLogo(R.mipmap.ic_sunny);
                        } else if (weather.contains("雨")) {
                            toolbar.setLogo(R.mipmap.ic_rainy);
                        } else if (weather.contains("雪")) {
                            toolbar.setLogo(R.mipmap.ic_snowy);
                        }
                        toolbar.setTitle("   " + weather + "   " + mBean.getTemperature() + "℃");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar(view);
        initTabLayout(view);
        inflateMenu();
        initSearchView();
        initView();
        initData();
    }

    private void initToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.t_tab);
        toolbar.setTitle("   " + getResources().getString(R.string.app_name));
        toolbar.setLogo(R.mipmap.ic_cloudy);
        toolbar.setNavigationIcon(null);
    }

    private void initTabLayout(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.vp_tab);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.getTabAt(0).setCustomView(setTabTextIcon("工具", R.mipmap.ic_tool));
        tabLayout.getTabAt(1).setCustomView(setTabTextIcon("资讯", R.mipmap.ic_info));
    }

    private View setTabTextIcon(String text, int icon) {
        View newtab = LayoutInflater.from(getActivity()).inflate(R.layout.item_tab, null);
        TextView tv = (TextView) newtab.findViewById(R.id.tv_tabtext);
        tv.setText(text);
        ImageView iv = (ImageView) newtab.findViewById(R.id.iv_tabicon);
        iv.setImageResource(icon);
        return newtab;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        Fragment newfragment = new BlankFragment();
        Bundle data = new Bundle();
        data.putInt("index", 0);
        newfragment.setArguments(data);
        adapter.addFlag(newfragment, "");

        newfragment = new BlankFragment();
        data = new Bundle();
        data.putInt("index", 1);
        newfragment.setArguments(data);
        adapter.addFlag(newfragment, "");

        viewPager.setAdapter(adapter);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            index = bundle.getInt(getString(R.string.index));
        }
        viewPager.setCurrentItem(index, true);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFlag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void initView() {
    }

    private void initData() {
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.spf_user), Context.MODE_PRIVATE);
        if (getActivity().getIntent().getStringExtra("city") != null) {
            city = getActivity().getIntent().getStringExtra("city");
            editor = sharedPreferences.edit();
            editor.putString("city", city);
            editor.apply();
        } else {
            city = sharedPreferences.getString("city", "CHBJ000000");
        }
        new Thread(loadWeather).start();
    }

    private void inflateMenu() {
        toolbar.inflateMenu(R.menu.menu_pocket);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_toy:
                        Intent intent1 = new Intent();
                        intent1.setClass(getActivity(), DragonActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.menu_setting:
                        Intent intent2 = new Intent();
                        intent2.setClass(getActivity(), SettingActivity.class);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        });
    }

    private void initSearchView() {
        final SearchView searchView = (SearchView) toolbar.getMenu()
                .findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint("搜索...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent();
                intent.setAction(ACTION_VIEW);
                String URL = "http://www.baidu.com/s?wd=";
                intent.setData(Uri.parse(URL + query));
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
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
