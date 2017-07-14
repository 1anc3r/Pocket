package me.lancer.pocket.info.mvp.novel.fragment;

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
import me.lancer.pocket.info.mvp.base.fragment.PresenterFragment;
import me.lancer.pocket.info.mvp.novel.NovelBean;
import me.lancer.pocket.info.mvp.novel.NovelPresenter;
import me.lancer.pocket.info.mvp.novel.INovelView;
import me.lancer.pocket.info.mvp.novel.adapter.NovelTxtAdapter;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class NovelRankFragment extends PresenterFragment<NovelPresenter> implements INovelView {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private NovelTxtAdapter mAdapter;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private List<NovelBean> mList = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 1:
                    mSwipeRefreshLayout.setRefreshing(true);
                    break;
                case 2:
                    break;
                case 3:
                    if (msg.obj != null) {
                        mList.clear();
                        mList.addAll((List<NovelBean>) msg.obj);
                        mAdapter.notifyDataSetChanged();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    private Runnable loadTop = new Runnable() {
        @Override
        public void run() {
            presenter.loadRank();
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

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(loadTop).start();
//                Message msg = new Message();
//                msg.what = 0;
//                handler.sendMessageDelayed(msg, 800);
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NovelTxtAdapter(getActivity(), mList, 1);
//        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected NovelPresenter onCreatePresenter() {
        return new NovelPresenter(this);
    }

    @Override
    public void showRank(List<NovelBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showCate(List<NovelBean> list) {

    }

    @Override
    public void showSearch(List<NovelBean> list) {

    }

    @Override
    public void showDetail(NovelBean bean) {

    }

    @Override
    public void showChapter(List<NovelBean.Chapters> list) {

    }

    @Override
    public void showContent(String content) {

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
