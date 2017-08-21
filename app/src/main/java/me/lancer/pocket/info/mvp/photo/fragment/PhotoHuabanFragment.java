package me.lancer.pocket.info.mvp.photo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.photo.IPhotoView;
import me.lancer.pocket.info.mvp.photo.PhotoBean;
import me.lancer.pocket.info.mvp.photo.PhotoPresenter;
import me.lancer.pocket.info.mvp.photo.adapter.PhotoAdapter;
import me.lancer.pocket.ui.mvp.base.fragment.PresenterLazyLoadFragment;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class PhotoHuabanFragment extends PresenterLazyLoadFragment<PhotoPresenter> implements IPhotoView {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private PhotoAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private List<PhotoBean> list = new ArrayList<>();

    private int type = 0, last = 0;
    private String maxId = "";

    private Runnable loadHuaban = new Runnable() {
        @Override
        public void run() {
            presenter.loadHuaban(type, maxId);
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
                        if (maxId.equals("")) {
                            list.clear();
                            list.addAll((List<PhotoBean>) msg.obj);
                            adapter.notifyDataSetChanged();
                        } else {
                            int size = list.size();
                            list.addAll((List<PhotoBean>) msg.obj);
                            for (int i = 0; i < 70; i++) {
                                adapter.notifyItemInserted(size + i);
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

        Bundle data = this.getArguments();
        type = data.getInt("id");
        initView(view);
    }

    @Override
    public void fetchData() {
        initData();
    }

    private void initData() {
        new Thread(loadHuaban).start();
    }

    private void initView(View view) {

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                maxId = "";
                new Thread(loadHuaban).start();
            }
        });
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        adapter = new PhotoAdapter(getActivity(), list);
        rvList.setAdapter(adapter);
        rvList.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && last + 1 == adapter.getItemCount()) {
                    maxId = list.get(list.size() - 1).getTitle();
                    new Thread(loadHuaban).start();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                last = getMax(layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]));
            }
        });
    }

    private int getMax(int[] arr) {
        int len = arr.length;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < len; i++) {
            max = Math.max(max, arr[i]);
        }
        return max;
    }

    @Override
    protected PhotoPresenter onCreatePresenter() {
        return new PhotoPresenter(this);
    }

    @Override
    public void showPexels(List<PhotoBean> list) {

    }

    @Override
    public void showGank(List<PhotoBean> list) {

    }

    @Override
    public void showHuaban(List<PhotoBean> list) {
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
