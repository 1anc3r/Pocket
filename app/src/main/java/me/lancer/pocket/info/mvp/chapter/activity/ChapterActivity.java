package me.lancer.pocket.info.mvp.chapter.activity;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.chapter.ChapterBean;
import me.lancer.pocket.info.mvp.chapter.ChapterPresenter;
import me.lancer.pocket.info.mvp.chapter.IChapterView;
import me.lancer.pocket.info.mvp.chapter.adapter.ChapterAdapter;
import me.lancer.pocket.ui.application.Params;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.ui.mvp.collect.CollectBean;
import me.lancer.pocket.ui.mvp.collect.CollectUtil;

public class ChapterActivity extends PresenterActivity<ChapterPresenter> implements IChapterView {

    private Toolbar toolbar;
    private ImageView ivCover;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private ChapterAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private List<ChapterBean> list = new ArrayList<>();

    private FloatingActionButton fabCollect;
    private List<CollectBean> temps = new ArrayList<>();
    private CollectBean temp = new CollectBean();

    private String link, title, cover;

    private Runnable loadTop = new Runnable() {
        @Override
        public void run() {
            presenter.loadList(link);
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
                        list = (List<ChapterBean>) msg.obj;
                        adapter = new ChapterAdapter(ChapterActivity.this, list);
                        rvList.setAdapter(adapter);
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        }
    };

    View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == fabCollect) {
                if (temps.size() == 1) {
                    fabCollect.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
                    CollectUtil.delete(temps.get(0));
                    temps = CollectUtil.query(title, link);
                } else {
                    fabCollect.setImageResource(R.mipmap.ic_favorite_white_24dp);
                    temp.setType(2);
                    temp.setCate(11);
                    temp.setModel(8);
                    temp.setCover(cover);
                    temp.setTitle(title);
                    temp.setLink(link);
                    CollectUtil.add(temp);
                    temps = CollectUtil.query(title, link);
                }
            }
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ivCover = (ImageView) findViewById(R.id.iv_cover);
        ViewCompat.setTransitionName(ivCover, Params.TRANSITION_PIC);
        Glide.with(this).load(cover).into(ivCover);
        fabCollect = (FloatingActionButton) findViewById(R.id.fab_collect);
        fabCollect.setVisibility(View.VISIBLE);
        fabCollect.setOnClickListener(vOnClickListener);
        temps = CollectUtil.query(title, link);
        if (temps.size() == 1) {
            fabCollect.setImageResource(R.mipmap.ic_favorite_white_24dp);
        } else {
            fabCollect.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
        }
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.srl);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessageDelayed(msg, 800);
            }
        });
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        adapter = new ChapterAdapter(this, list);
        rvList.setAdapter(adapter);
        new Thread(loadTop).start();
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

    public static void startActivity(Activity activity, String link, String cover, String title, String category, ImageView ImageView) {
        Intent intent = new Intent();
        intent.setClass(activity, ChapterActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("title", title);
        intent.putExtra("cover", cover);
        intent.putExtra("category", category);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, ImageView, "transitionPic");
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected ChapterPresenter onCreatePresenter() {
        return new ChapterPresenter(this);
    }

    @Override
    public void showList(List<ChapterBean> list) {
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
