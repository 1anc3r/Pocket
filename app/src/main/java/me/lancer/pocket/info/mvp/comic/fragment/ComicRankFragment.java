package me.lancer.pocket.info.mvp.comic.fragment;

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

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.comic.ComicBean;
import me.lancer.pocket.info.mvp.comic.ComicPresenter;
import me.lancer.pocket.info.mvp.comic.IComicView;
import me.lancer.pocket.info.mvp.comic.adapter.ComicAdapter;
import me.lancer.pocket.ui.mvp.base.fragment.PresenterLazyLoadFragment;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class ComicRankFragment extends PresenterLazyLoadFragment<ComicPresenter> implements IComicView {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private ComicAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private List<ComicBean> list = new ArrayList<>();

    private Runnable loadTop = new Runnable() {
        @Override
        public void run() {
            presenter.loadRankTitle();
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
                        list.addAll((List<ComicBean>) msg.obj);
                        adapter.notifyDataSetChanged();
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book, container, false);
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
        new Thread(loadTop).start();
    }

    private void initView(View view) {

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(loadTop).start();
            }
        });
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        adapter = new ComicAdapter(getActivity(), list);
        rvList.setAdapter(adapter);
    }

    @Override
    protected ComicPresenter onCreatePresenter() {
        return new ComicPresenter(this);
    }


    @Override
    public void showList(List<ComicBean> list) {
    }

    @Override
    public void showRank(List<ComicBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showSort(List<ComicBean> list) {

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
