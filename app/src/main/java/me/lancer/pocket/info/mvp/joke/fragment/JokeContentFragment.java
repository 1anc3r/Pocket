package me.lancer.pocket.info.mvp.joke.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.joke.IJokeView;
import me.lancer.pocket.info.mvp.joke.JokeBean;
import me.lancer.pocket.info.mvp.joke.JokePresenter;
import me.lancer.pocket.info.mvp.joke.adapter.JokeAdapter;
import me.lancer.pocket.ui.mvp.base.fragment.PresenterFragment;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class JokeContentFragment extends PresenterFragment<JokePresenter> implements IJokeView {

    private FloatingActionButton fabRefresh;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private JokeAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<JokeBean> list = new ArrayList<>();

    private int type = 0;

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
                        list.addAll((List<JokeBean>) msg.obj);
                        adapter = new JokeAdapter(getActivity(), list);
                        rvList.setAdapter(adapter);
                    }
                    swipeRefresh.setRefreshing(false);
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

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_list);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        adapter = new JokeAdapter(getActivity(), list);
        adapter.setHasStableIds(true);
        rvList.setAdapter(adapter);
        fabRefresh = (FloatingActionButton) view.findViewById(R.id.fab_refresh);
        fabRefresh.setVisibility(View.VISIBLE);
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {
                    new Thread(loadText).start();
                } else if (type == 1) {
                    new Thread(loadImage).start();
                } else if (type == 2) {
                    new Thread(loadVideo).start();
                }
            }
        });
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

