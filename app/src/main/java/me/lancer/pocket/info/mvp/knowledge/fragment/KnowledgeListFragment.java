package me.lancer.pocket.info.mvp.knowledge.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.base.fragment.PresenterFragment;
import me.lancer.pocket.info.mvp.knowledge.IKnowledgeView;
import me.lancer.pocket.info.mvp.knowledge.KnowledgeBean;
import me.lancer.pocket.info.mvp.knowledge.KnowledgePresenter;
import me.lancer.pocket.info.mvp.knowledge.adapter.ClassifyAdapter;
import me.lancer.pocket.info.mvp.knowledge.adapter.KnowledgeAdapter;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class KnowledgeListFragment extends PresenterFragment<KnowledgePresenter> implements IKnowledgeView {

    private RecyclerView rvMsg, rvClass;

    private KnowledgeAdapter adpMsg;
    private ClassifyAdapter adpClass;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private List<KnowledgeBean> lMsg = new ArrayList<>();
    private List<KnowledgeBean> lClass = new ArrayList<>();

    private int type, id = 11;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 1:
//                    mSwipeRefreshLayout.setRefreshing(true);
                    break;
                case 2:
                    Log.e("log", (String) msg.obj);
                    break;
                case 3:
                    if (msg.obj != null) {
                        lClass.clear();
                        lClass.addAll((List<KnowledgeBean>) msg.obj);
                        adpClass = new ClassifyAdapter(getActivity(), handler, type, lClass);
                        rvClass.setAdapter(adpClass);
                    }
//                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 4:
                    if (msg.obj != null) {
                        lMsg.clear();
                        lMsg.addAll((List<KnowledgeBean>) msg.obj);
                        adpMsg.notifyDataSetChanged();
                    }
                    break;
                case 5:
                    if (msg.obj != null) {
                        id = (int) msg.obj;
                        new Thread(loadList).start();
                    }
                    break;
            }
        }
    };

    private Runnable loadList = new Runnable() {
        @Override
        public void run() {
            presenter.loadList(type, id);
        }
    };

    private Runnable loadClass = new Runnable() {
        @Override
        public void run() {
            presenter.loadClassify(type);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_knowledge, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initData() {
        type = getArguments().getInt("id");
        new Thread(loadClass).start();
        new Thread(loadList).start();
    }

    private void initView(View view) {

//        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_list);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Message msg = new Message();
//                msg.what = 0;
//                handler.sendMessageDelayed(msg, 800);
//            }
//        });
        rvMsg = (RecyclerView) view.findViewById(R.id.rv_list);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvMsg.setLayoutManager(mStaggeredGridLayoutManager);
        adpMsg = new KnowledgeAdapter(getActivity(), type, lMsg);
        rvMsg.setAdapter(adpMsg);

        rvClass = (RecyclerView) view.findViewById(R.id.rv_class);
        rvClass.setVisibility(View.VISIBLE);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvClass.setLayoutManager(mLinearLayoutManager);
        adpClass = new ClassifyAdapter(getActivity(), handler, type, lMsg);
        rvClass.setAdapter(adpClass);
    }

    @Override
    protected KnowledgePresenter onCreatePresenter() {
        return new KnowledgePresenter(this);
    }

    @Override
    public void showClassify(List<KnowledgeBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showList(List<KnowledgeBean> list) {
        Message msg = new Message();
        msg.what = 4;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showNews(List<KnowledgeBean> list) {
        Message msg = new Message();
        msg.what = 5;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showShow(KnowledgeBean bean) {

    }

    @Override
    public void showSearch(List<KnowledgeBean> list) {

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

