package me.lancer.pocket.info.mvp.photo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.page.fragment.PagerFragment;
import me.lancer.pocket.ui.mvp.base.activity.BaseActivity;

public class PhotoGalleryActivity extends BaseActivity {

    private List<String> mData = new ArrayList<String>();
    private PageAdapter adapter;
    private ViewPager viewPager;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        init();
    }

    public void init() {
        mData = getIntent().getStringArrayListExtra("gallery");
        position = getIntent().getIntExtra("position", 0);
        adapter = new PageAdapter(mData, getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class PageAdapter extends FragmentPagerAdapter {

        List<String> data;

        public PageAdapter(List<String> data, FragmentManager fm) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {
            return PagerFragment.newInstance(data.get(position));
        }

        @Override
        public int getCount() {
            return data.size();
        }

        public void refreshData(List<String> newData) {
            data = newData;
            notifyDataSetChanged();
        }
    }
}
