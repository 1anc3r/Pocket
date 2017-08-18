package me.lancer.pocket.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.mvp.base.fragment.PresenterFragment;
import me.lancer.pocket.ui.mvp.collect.CollectAdapter;
import me.lancer.pocket.ui.mvp.collect.CollectBean;
import me.lancer.pocket.ui.mvp.collect.CollectPresenter;
import me.lancer.pocket.ui.mvp.collect.ICollectView;

public class CollectFragment extends PresenterFragment<CollectPresenter> implements ICollectView, CollectAdapter.MyItemClickListener, CollectAdapter.MyItemLongClickListener {

    private Toolbar toolbar;
    private ImageView ivImg;
    private RecyclerView mRecyclerView;
    private CollectAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<CollectBean> mList = new ArrayList<>();
    private CollectBean temp;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    if (msg.obj != null) {
                        mList.clear();
                        mList.addAll((List<CollectBean>) msg.obj);
                        Log.e("Collect: ", ""+mList.size());
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    private Runnable query = new Runnable() {
        @Override
        public void run() {
            presenter.query();
        }
    };

    private Runnable add = new Runnable() {
        @Override
        public void run() {
            presenter.add(temp);
        }
    };

    private Runnable modify = new Runnable() {
        @Override
        public void run() {
            presenter.modify(temp);
        }
    };

    private Runnable delete = new Runnable() {
        @Override
        public void run() {
            presenter.delete(temp);
        }
    };

    public CollectFragment() {
    }

    public static CollectFragment newInstance(String param1, String param2) {
        CollectFragment fragment = new CollectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collect, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initData() {
        new Thread(query).start();
    }

    private void initView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("收藏");
        ivImg = (ImageView) view.findViewById(R.id.iv_img);
        if ((Math.random() * 16) > 8) {
            Glide.with(this).load("https://raw.githubusercontent.com/1anc3r/Pocket/master/summer.gif").into(ivImg);
        } else {
            Glide.with(this).load("https://raw.githubusercontent.com/1anc3r/Pocket/master/winter.gif").into(ivImg);
        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CollectAdapter(getActivity(), mList);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setFocusable(true);
        mRecyclerView.setFocusableInTouchMode(true);
    }

    @Override
    public void onItemClick(View view, int postion) {

    }

    @Override
    public void onItemLongClick(View view, final int postion) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                CollectBean bean = new CollectBean();
//                bean.setType(index);
//                bean.setCate(postion);
//                bean.setTitle(mList.get(postion).getName());
//            }
//        }).start();
    }

    @Override
    protected CollectPresenter onCreatePresenter() {
        return new CollectPresenter(this);
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

    @Override
    public void showResult(int result) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = result;
        handler.sendMessage(msg);
    }

    @Override
    public void showResult(long result) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = result;
        handler.sendMessage(msg);
    }

    @Override
    public void showList(List<CollectBean> list) {
        Message msg = new Message();
        msg.what = 4;
        msg.obj = list;
        handler.sendMessage(msg);
    }
}
