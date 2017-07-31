package me.lancer.pocket.info.mvp.photo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.base.fragment.BaseFragment;

public class PhotoFragment extends BaseFragment {

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
        toolbar = (Toolbar) view.findViewById(R.id.t_tab);
        toolbar.setTitle("图片");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
//        ((BlankActivity) getActivity()).initDrawer(toolbar);
        initTabLayout(view);
        inflateMenu();
        initSearchView();
        initView();
        initData();
    }

    private void initTabLayout(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.vp_tab);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        Fragment newfragment = new PhotoWelfareFragment();
        Bundle data = new Bundle();
        data.putInt("id", 0);
        data.putString("title", "妹子");
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "妹子");

        newfragment = new PhotoTopFragment();
        data = new Bundle();
        data.putInt("id", 0);
        data.putString("title", "美景");
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "美景");

//        newfragment = new PhotoThemeFragment();
//        data = new Bundle();
//        data.putInt("id", 0);
//        data.putString("title", "分类");
//        newfragment.setArguments(data);
//        adapter.addFrag(newfragment, "分类");

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

        public void addFrag(Fragment fragment, String title) {
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
        toolbar.inflateMenu(R.menu.menu_normal);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_about:
                        showAboutDialog();
                        break;
                }
                return true;
            }
        });
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("图片");
        builder.setMessage(
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 妹子 : 好看的妹子图\n" +
                "\t\t\t\t * 美景 : 好看的风景照\n" +
                "\t\t\t\t * ——数据来源 : " +
                "\n\t\t\t\t *  佳人 : Gank.io（gank.io）" +
                "\n\t\t\t\t *  美图 : Pexels（www.pexels.com）\n" +
                "\t\t\t\t */");
        builder.show();
    }

//    private void inflateMenu() {
//        toolbar.inflateMenu(R.menu.menu_search);
//    }

    private void initSearchView() {
        final SearchView searchView = (SearchView) toolbar.getMenu()
                .findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint("搜索...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}
