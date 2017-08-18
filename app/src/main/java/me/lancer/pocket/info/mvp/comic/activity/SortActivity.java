package me.lancer.pocket.info.mvp.comic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.info.mvp.comic.ComicBean;
import me.lancer.pocket.info.mvp.comic.ComicPresenter;
import me.lancer.pocket.info.mvp.comic.IComicView;
import me.lancer.pocket.info.mvp.comic.adapter.ComicAdapter;
import me.lancer.pocket.ui.application.Params;
import me.lancer.pocket.ui.mvp.collect.CollectBean;
import me.lancer.pocket.ui.mvp.collect.CollectUtil;

public class SortActivity extends PresenterActivity<ComicPresenter> implements IComicView {

    private String link, title, cover;
    private Toolbar toolbar;
    private ImageView ivCover;
    private List<ComicBean> mData = new ArrayList<ComicBean>();
    private ComicAdapter mAdapter;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton fab;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 1:
                    mSwipeRefreshLayout.setRefreshing(true);
                    break;
                case 2:
                    break;
                case 3:
                    if (msg.obj != null) {
                        mData = (List<ComicBean>) msg.obj;
                        mAdapter = new ComicAdapter(SortActivity.this, mData);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    private Runnable loadTop = new Runnable() {
        @Override
        public void run() {
            presenter.loadSortContent(link);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        init();
    }

    public void init() {
        link = getIntent().getStringExtra("link");
        title = getIntent().getStringExtra("title");
        cover = getIntent().getStringExtra("cover");
        Log.e("init: ", title+link);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ivCover = (ImageView) findViewById(R.id.imageView);
        ViewCompat.setTransitionName(ivCover, Params.TRANSITION_PIC);
        Glide.with(this).load(cover).into(ivCover);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                new Thread(loadTop).start();
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessageDelayed(msg, 800);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ComicAdapter(this, mData);
        mRecyclerView.setAdapter(mAdapter);
        new Thread(loadTop).start();
    }

    public static void startActivity(Activity activity, String link, String cover, String title, ImageView ImageView) {
        Intent intent = new Intent();
        intent.setClass(activity, SortActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("title", title);
        intent.putExtra("cover", cover);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, ImageView, "transitionPic");
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected ComicPresenter onCreatePresenter() {
        return new ComicPresenter(this);
    }

    @Override
    public void showList(List<ComicBean> list) {
    }

    @Override
    public void showRank(List<ComicBean> list) {
    }

    @Override
    public void showSort(List<ComicBean> list) {
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
