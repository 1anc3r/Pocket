package me.lancer.pocket.info.mvp.joke.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.base.fragment.PresenterFragment;
import me.lancer.pocket.info.mvp.joke.IJokeView;
import me.lancer.pocket.info.mvp.joke.JokeBean;
import me.lancer.pocket.info.mvp.joke.JokePresenter;
import me.lancer.pocket.info.mvp.joke.adapter.JokeAdapter;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class JokeContentFragment extends PresenterFragment<JokePresenter> implements IJokeView {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private JokeAdapter mAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private List<JokeBean> mList = new ArrayList<>();

    private int type = 0;

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
                        mList.addAll((List<JokeBean>) msg.obj);
                        mAdapter = new JokeAdapter(getActivity(), mList);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    private Runnable loadText = new Runnable() {
        @Override
        public void run() {
            presenter.loadText();
        }
    };

    private Runnable loadImage = new Runnable() {
        @Override
        public void run() {
            presenter.loadImage();
        }
    };

    private Runnable loadVideo = new Runnable() {
        @Override
        public void run() {
            presenter.loadVideo();
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
        initData();
    }

    private void initData() {
        type = getArguments().getInt("id", 0);
        if (type == 0) {
            new Thread(loadText).start();
        } else if (type == 1) {
            new Thread(loadImage).start();
        } else if (type == 2) {
            new Thread(loadVideo).start();
        }
    }

    private void initView(View view) {

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_list);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new JokeAdapter(getActivity(), mList);
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected JokePresenter onCreatePresenter() {
        return new JokePresenter(this);
    }

    @Override
    public void showText(List<JokeBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showImage(List<JokeBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showVideo(List<JokeBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
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

