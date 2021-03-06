package me.lancer.pocket.info.mvp.news.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.news.INewsView;
import me.lancer.pocket.info.mvp.news.NewsBean;
import me.lancer.pocket.info.mvp.news.NewsPresenter;
import me.lancer.pocket.info.mvp.news.adapter.NewsAdapter;
import me.lancer.pocket.ui.mvp.base.fragment.PresenterLazyLoadFragment;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class NewsLatestFragment extends PresenterLazyLoadFragment<NewsPresenter> implements INewsView {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private NewsAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private List<NewsBean> list = new ArrayList<>();

    private int last = 0, flag = 0, load = 0;
    private String date;

    private Runnable loadLatest = new Runnable() {
        @Override
        public void run() {
            presenter.loadLatest();
        }
    };

    private Runnable loadBefore = new Runnable() {
        @Override
        public void run() {
            presenter.loadBefore(date);
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
                        list.clear();
                        list.add(new NewsBean(1, "— 今日 —"));
                        list.addAll((List<NewsBean>) msg.obj);
                        adapter.notifyDataSetChanged();
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
                case 4:
                    if (msg.obj != null && load == 1) {
                        int size = list.size();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                        date = formatter.format(new Date(System.currentTimeMillis() - 24 * 3600 * 1000 * (flag)));
                        date = date.substring(0, 4) + "年" + date.substring(4, 6) + "月" + date.substring(6, 8) + "日";
                        list.add(new NewsBean(1, "— " + date + " —"));
                        list.addAll((List<NewsBean>) msg.obj);
                        for (int i = 0; i < ((List<NewsBean>) msg.obj).size() + 1; i++) {
                            adapter.notifyItemInserted(size + 1 + i);
                        }
                        load = 0;
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
                case 5:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    @Override
    public void fetchData() {
        initData();
    }

    private void initData() {
        new Thread(loadLatest).start();
    }

    private void initView(View view) {

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flag = 0;
                new Thread(loadLatest).start();
            }
        });
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        adapter = new NewsAdapter(getActivity(), list);
        rvList.setAdapter(adapter);
        rvList.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && last + 1 == adapter.getItemCount()) {
                    load = 1;
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                    date = formatter.format(new Date(System.currentTimeMillis() - 24 * 3600 * 1000 * (flag++)));
                    new Thread(loadBefore).start();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                last = getMax(layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]));
            }
        });
    }

    private int getMax(int[] arr) {
        int len = arr.length;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < len; i++) {
            max = Math.max(max, arr[i]);
        }
        return max;
    }

    @Override
    protected NewsPresenter onCreatePresenter() {
        return new NewsPresenter(this);
    }


    @Override
    public void showItem(List<NewsBean> list) {

    }

    @Override
    public void showDetail(NewsBean bean) {

    }

    @Override
    public void showHotest(List<NewsBean> list) {
        Message msg = new Message();
        msg.what = 5;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showPublic(List<NewsBean> list) {

    }

    @Override
    public void showLatest(List<NewsBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showBefore(List<NewsBean> list) {
        Message msg = new Message();
        msg.what = 4;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showList(List<NewsBean> list) {

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

