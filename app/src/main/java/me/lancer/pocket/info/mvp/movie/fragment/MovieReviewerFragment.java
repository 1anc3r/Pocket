package me.lancer.pocket.info.mvp.movie.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import me.lancer.pocket.ui.mvp.base.fragment.PresenterFragment;
import me.lancer.pocket.info.mvp.movie.IMovieView;
import me.lancer.pocket.info.mvp.movie.MovieBean;
import me.lancer.pocket.info.mvp.movie.MoviePresenter;
import me.lancer.pocket.info.mvp.movie.adapter.MovieAdapter;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class MovieReviewerFragment extends PresenterFragment<MoviePresenter> implements IMovieView {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private MovieAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<MovieBean> mList = new ArrayList<>();

    private int pager = 0, last = 0;

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
                            mList = (List<MovieBean>) msg.obj;
                            mAdapter = new MovieAdapter(getActivity(), mList);
                            rvList.setAdapter(mAdapter);
                        } else {
                            mList.addAll((List<MovieBean>) msg.obj);
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

    private Runnable loadReviewer = new Runnable() {
        @Override
        public void run() {
            presenter.loadReviewer(pager);
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
        new Thread(loadReviewer).start();
    }

    private void initView(View view) {

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_list);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pager = 0;
                new Thread(loadReviewer).start();
            }
        });
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(mLinearLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        mAdapter = new MovieAdapter(getActivity(), mList);
        rvList.setAdapter(mAdapter);
        rvList.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && last + 1 == mAdapter.getItemCount()) {
                    pager += 20;
                    new Thread(loadReviewer).start();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                last = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected MoviePresenter onCreatePresenter() {
        return new MoviePresenter(this);
    }


    @Override
    public void showReviewer(List<MovieBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showTopMovie(List<MovieBean> list) {

    }

    @Override
    public void showReviewerDetail(MovieBean bean) {

    }

    @Override
    public void showTopDetail(MovieBean bean) {

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
