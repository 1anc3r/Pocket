package me.lancer.pocket.info.mvp.game.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.game.GameBean;
import me.lancer.pocket.info.mvp.game.GamePresenter;
import me.lancer.pocket.info.mvp.game.IGameView;
import me.lancer.pocket.info.mvp.game.adapter.GameAdapter;
import me.lancer.pocket.ui.mvp.base.fragment.PresenterFragment;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class GameFeaturedFragment extends PresenterFragment<GamePresenter> implements IGameView {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private GameAdapter adapter;
    private GridLayoutManager mGridLayoutManager;
    private List<GameBean> list = new ArrayList<>();

    private int pager = 0, last = 0;

    private Runnable loadFeatured = new Runnable() {
        @Override
        public void run() {
            presenter.loadFeatured();
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
                        if (pager == 0) {
                            list = (List<GameBean>) msg.obj;
                            adapter = new GameAdapter(getActivity(), 1, list);
                            rvList.setAdapter(adapter);
                        } else {
                            list.addAll((List<GameBean>) msg.obj);
                            for (int i = 0; i < 10; i++) {
                                adapter.notifyItemInserted(pager * 10 + i);
                            }
                        }
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
        new Thread(loadFeatured).start();
    }

    private void initView(View view) {

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pager = 0;
                new Thread(loadFeatured).start();
            }
        });
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        mGridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(mGridLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        adapter = new GameAdapter(getActivity(), 1, list);
        adapter.setHasStableIds(true);
        rvList.setAdapter(adapter);
    }

    @Override
    protected GamePresenter onCreatePresenter() {
        return new GamePresenter(this);
    }


    @Override
    public void showFeatured(List<GameBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showCategories(List<GameBean> list) {

    }

    @Override
    public void showDetail(GameBean bean) {

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
