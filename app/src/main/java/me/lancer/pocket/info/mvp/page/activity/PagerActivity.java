package me.lancer.pocket.info.mvp.page.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.page.IPageView;
import me.lancer.pocket.info.mvp.page.PageBean;
import me.lancer.pocket.info.mvp.page.PagePresenter;
import me.lancer.pocket.info.mvp.page.fragment.PagerNoBarFragment;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;

public class PagerActivity extends PresenterActivity<PagePresenter> implements IPageView {

    private String link;
    private List<PageBean> list = new ArrayList<PageBean>();
    private PageAdapter adapter;
    private ViewPager viewPager;

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
                        list = (List<PageBean>) msg.obj;
                        adapter.refreshData(list);
                    }
                    break;
            }
        }
    };

    private Runnable loadTop = new Runnable() {
        @Override
        public void run() {
            presenter.loadList(link);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        init();
    }

    public void init() {
        link = getIntent().getStringExtra("link");
        adapter = new PageAdapter(list, getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        new Thread(loadTop).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected PagePresenter onCreatePresenter() {
        return new PagePresenter(this);
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
    public void showList(List<PageBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    class PageAdapter extends FragmentPagerAdapter {

        List<PageBean> data;

        public PageAdapter(List<PageBean> data, FragmentManager fm) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {
            return PagerNoBarFragment.newInstance(data.get(position).getLink());
        }

        @Override
        public int getCount() {
            return data.size();
        }

        public void refreshData(List<PageBean> newData) {
            data = newData;
            notifyDataSetChanged();
        }
    }
}
