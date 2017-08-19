package me.lancer.pocket.info.mvp.video.fragment;

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
import me.lancer.pocket.info.mvp.video.IVideoView;
import me.lancer.pocket.info.mvp.video.VideoBean;
import me.lancer.pocket.info.mvp.video.VideoPresenter;
import me.lancer.pocket.info.mvp.video.adapter.VideoAdapter;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class VideoThemeFragment extends PresenterFragment<VideoPresenter> implements IVideoView {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private VideoAdapter mAdapter;
    private StaggeredGridLayoutManager layoutManager;
    private List<VideoBean> mList = new ArrayList<>();

    private int pager = 0;

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
                        mList.addAll((List<VideoBean>) msg.obj);
                        mAdapter.notifyDataSetChanged();
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        }
    };

    private Runnable loadTheme = new Runnable() {
        @Override
        public void run() {
            presenter.loadTheme(pager);
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
        Bundle data = this.getArguments();
        pager = data.getInt("id");
        initView(view);
        initData();
    }

    private void initData() {
        new Thread(loadTheme).start();
    }

    private void initView(View view) {

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_list);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(loadTheme).start();
            }
        });
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new VideoAdapter(getActivity(), mList);
        rvList.setAdapter(mAdapter);
    }

    @Override
    protected VideoPresenter onCreatePresenter() {
        return new VideoPresenter(this);
    }

    @Override
    public void showTheme(List<VideoBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showDetail(VideoBean bean) {

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
