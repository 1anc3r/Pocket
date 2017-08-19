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
import me.lancer.pocket.ui.mvp.base.fragment.PresenterFragment;
import me.lancer.pocket.info.mvp.comic.ComicBean;
import me.lancer.pocket.info.mvp.comic.ComicPresenter;
import me.lancer.pocket.info.mvp.comic.IComicView;
import me.lancer.pocket.info.mvp.comic.adapter.ComicAdapter;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class ComicListFragment extends PresenterFragment<ComicPresenter> implements IComicView {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private ComicAdapter mAdapter;
    private StaggeredGridLayoutManager layoutManager;
    private List<ComicBean> mList = new ArrayList<>();

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
                        mList.clear();
                        mList.addAll((List<ComicBean>) msg.obj);
                        mAdapter.notifyDataSetChanged();
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        }
    };

    private Runnable loadTop = new Runnable() {
        @Override
        public void run() {
            presenter.loadList();
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
        initData();
    }

    private void initData() {
        new Thread(loadTop).start();
    }

    private void initView(View view) {

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(loadTop).start();
            }
        });
        rvList = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ComicAdapter(getActivity(), mList);
        rvList.setAdapter(mAdapter);
    }

    @Override
    protected ComicPresenter onCreatePresenter() {
        return new ComicPresenter(this);
    }

    @Override
    public void showList(List<ComicBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showRank(List<ComicBean> list) {
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
