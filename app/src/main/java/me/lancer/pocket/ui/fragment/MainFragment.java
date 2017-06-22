package me.lancer.pocket.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.base.fragment.BaseFragment;
import me.lancer.pocket.ui.activity.MainActivity;
import me.lancer.pocket.ui.activity.SettingActivity;
import me.lancer.pocket.util.DensityUtil;

import static android.content.Intent.ACTION_VIEW;

public class MainFragment extends BaseFragment {

    private Toolbar toolbar;
    private int index = 0;

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
        toolbar.setTitle("口袋");
        toolbar.setNavigationIcon(null);
    }

    private void initTabLayout(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.vp_tab);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.getTabAt(0).setCustomView(setTabTextIcon("工具",R.mipmap.ic_tool));
        tabLayout.getTabAt(1).setCustomView(setTabTextIcon("资讯",R.mipmap.ic_info));
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

    }

    private void inflateMenu() {
        toolbar.inflateMenu(R.menu.menu_pocket);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_setting:
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), SettingActivity.class);
                        startActivity(intent);
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
}
