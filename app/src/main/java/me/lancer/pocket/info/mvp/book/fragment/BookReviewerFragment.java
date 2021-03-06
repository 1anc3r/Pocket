package me.lancer.pocket.info.mvp.book.fragment;

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
import me.lancer.pocket.info.mvp.book.BookBean;
import me.lancer.pocket.info.mvp.book.BookPresenter;
import me.lancer.pocket.info.mvp.book.IBookView;
import me.lancer.pocket.info.mvp.book.adapter.BookAdapter;
import me.lancer.pocket.ui.mvp.base.fragment.PresenterLazyLoadFragment;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class BookReviewerFragment extends PresenterLazyLoadFragment<BookPresenter> implements IBookView {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private BookAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<BookBean> list = new ArrayList<>();

    private int pager = 0, last = 0;

    private Runnable loadReviewer = new Runnable() {
        @Override
        public void run() {
            presenter.loadReviewer(pager);
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
                            list = (List<BookBean>) msg.obj;
                            adapter = new BookAdapter(getActivity(), list);
                            rvList.setAdapter(adapter);
                        } else {
                            list.addAll((List<BookBean>) msg.obj);
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
    }

    @Override
    public void fetchData() {
        initData();
    }

    private void initData() {
        new Thread(loadReviewer).start();
    }

    private void initView(View view) {

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pager = 0;
                new Thread(loadReviewer).start();
            }
        });
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        adapter = new BookAdapter(getActivity(), list);
        adapter.setHasStableIds(true);
        rvList.setAdapter(adapter);
        rvList.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && last + 1 == adapter.getItemCount()) {
                    pager += 20;
                    new Thread(loadReviewer).start();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                last = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected BookPresenter onCreatePresenter() {
        return new BookPresenter(this);
    }


    @Override
    public void showReviewer(List<BookBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showTopBook(List<BookBean> list) {

    }

    @Override
    public void showReviewerDetail(BookBean bean) {

    }

    @Override
    public void showTopDetail(BookBean bean) {

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
