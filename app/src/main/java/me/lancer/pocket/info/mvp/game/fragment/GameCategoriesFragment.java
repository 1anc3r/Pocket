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
import me.lancer.pocket.ui.mvp.base.fragment.PresenterFragment;
import me.lancer.pocket.info.mvp.game.GameBean;
import me.lancer.pocket.info.mvp.game.GamePresenter;
import me.lancer.pocket.info.mvp.game.IGameView;
import me.lancer.pocket.info.mvp.game.adapter.GameAdapter;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class GameCategoriesFragment extends PresenterFragment<GamePresenter> implements IGameView {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private GameAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private List<GameBean> mList = new ArrayList<>();

    private int pager = 0, last = 0, type = 0;
    private String[] keywords = {"specials", "top_sellers", "new_releases", "coming_soon"};

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
                            mList = (List<GameBean>) msg.obj;
                            mAdapter = new GameAdapter(getActivity(), 1, mList);
                            rvList.setAdapter(mAdapter);
                        } else {
                            mList.addAll((List<GameBean>) msg.obj);
                            for (int i = 0; i < 10; i++) {
                                mAdapter.notifyItemInserted(pager * 10 + i);
                            }
                        }
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        }
    };

    private Runnable loadCategories = new Runnable() {
        @Override
        public void run() {
            presenter.loadCategories(keywords[type]);
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
        type = data.getInt("id") - 1;
        initView(view);
        initData();
    }

    private void initData() {
        new Thread(loadCategories).start();
    }

    private void initView(View view) {

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_list);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pager = 0;
                new Thread(loadCategories).start();
            }
        });
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        mGridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(mGridLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        mAdapter = new GameAdapter(getActivity(), 1, mList);
        mAdapter.setHasStableIds(true);
        rvList.setAdapter(mAdapter);
    }

    @Override
    protected GamePresenter onCreatePresenter() {
        return new GamePresenter(this);
    }


    @Override
    public void showCategories(List<GameBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showFeatured(List<GameBean> list) {

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
