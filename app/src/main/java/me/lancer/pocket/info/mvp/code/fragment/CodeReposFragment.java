package me.lancer.pocket.info.mvp.code.fragment;

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
import me.lancer.pocket.info.mvp.code.CodeBean;
import me.lancer.pocket.info.mvp.code.CodePresenter;
import me.lancer.pocket.info.mvp.code.ICodeView;
import me.lancer.pocket.info.mvp.code.adapter.CodeAdapter;
import me.lancer.pocket.ui.mvp.base.fragment.PresenterFragment;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class CodeReposFragment extends PresenterFragment<CodePresenter> implements ICodeView {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private CodeAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<CodeBean> list = new ArrayList<>();

    private int pager = 1, last = 0;

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
                        if (pager == 1) {
                            list = (List<CodeBean>) msg.obj;
                            adapter = new CodeAdapter(getActivity(), list);
                            rvList.setAdapter(adapter);
                        } else {
                            list.addAll((List<CodeBean>) msg.obj);
                            for (int i = 0; i < 100; i++) {
                                adapter.notifyItemInserted(pager * 100 + i);
                            }
                        }
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        }
    };

    private Runnable loadRepositories = new Runnable() {
        @Override
        public void run() {
            presenter.loadRepositories(pager);
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
        new Thread(loadRepositories).start();
    }

    private void initView(View view) {

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_list);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pager = 1;
                new Thread(loadRepositories).start();
            }
        });
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        adapter = new CodeAdapter(getActivity(), list);
        rvList.setAdapter(adapter);
        rvList.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && last + 1 == adapter.getItemCount()) {
                    pager += 1;
                    new Thread(loadRepositories).start();
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
    protected CodePresenter onCreatePresenter() {
        return new CodePresenter(this);
    }


    @Override
    public void showUsers(List<CodeBean> list) {

    }

    @Override
    public void showOrganizations(List<CodeBean> list) {

    }

    @Override
    public void showRepositories(List<CodeBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showTrending(List<CodeBean> list) {

    }

    @Override
    public void showSearching(List<CodeBean> list) {

    }

    @Override
    public void showDetail(CodeBean bean) {

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
