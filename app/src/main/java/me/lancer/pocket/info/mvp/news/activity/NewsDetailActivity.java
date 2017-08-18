package me.lancer.pocket.info.mvp.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.steamcrafted.loadtoast.LoadToast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.info.mvp.news.INewsView;
import me.lancer.pocket.info.mvp.news.NewsBean;
import me.lancer.pocket.info.mvp.news.NewsPresenter;
import me.lancer.pocket.info.mvp.news.adapter.NewsAdapter;
import me.lancer.pocket.ui.application.Params;
import me.lancer.pocket.ui.mvp.collect.CollectBean;
import me.lancer.pocket.ui.mvp.collect.CollectUtil;
import me.lancer.pocket.ui.view.htmltextview.HtmlHttpImageGetter;
import me.lancer.pocket.ui.view.htmltextview.HtmlTextView;

public class NewsDetailActivity extends PresenterActivity<NewsPresenter> implements INewsView {

    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private List<NewsBean> mList = new ArrayList<>();

    private List<CollectBean> temps = new ArrayList<>();
    private CollectBean temp = new CollectBean();

    private CollapsingToolbarLayout layout;
    private FloatingActionButton fab;
    private ImageView ivImg;
    private HtmlTextView htvContent;
    private LoadToast loadToast;

    private int id;
    private String title, img, link;

    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    loadToast.error();
                    break;
                case 3:
                    if (msg.obj != null) {
                        loadToast.success();
                        NewsBean nb = (NewsBean) msg.obj;
                        layout.setTitle(nb.getTitle());
                        ViewCompat.setTransitionName(ivImg, Params.TRANSITION_PIC);
                        Glide.with(NewsDetailActivity.this).load(nb.getImg()).into(ivImg);
                        if (nb.getContent() != null) {
                            htvContent.setHtml(nb.getContent(), new HtmlHttpImageGetter(htvContent));
                        }
                    }
                    break;
                case 4:
                    if (msg.obj != null) {
                        loadToast.success();
                        mList.clear();
                        mList.addAll((List<NewsBean>) msg.obj);
                        mAdapter = new NewsAdapter(NewsDetailActivity.this, mList);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    break;
            }
        }
    };

    private Runnable loadDetail = new Runnable() {
        @Override
        public void run() {
            presenter.loadDetail(link);
        }
    };

    private Runnable loadItem = new Runnable() {
        @Override
        public void run() {
            presenter.loadItem(id);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large);
        initData();
        initView();
    }

    private void initData() {
        id = getIntent().getIntExtra("id", 2);
        title = getIntent().getStringExtra("title");
        img = getIntent().getStringExtra("img");
        link = getIntent().getStringExtra("link");
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        layout = (CollapsingToolbarLayout) findViewById(R.id.ctl_large);
        layout.setTitle(title);
        ivImg = (ImageView) findViewById(R.id.iv_img);
        ViewCompat.setTransitionName(ivImg, Params.TRANSITION_PIC);
        Glide.with(this).load(img).into(ivImg);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(vOnClickListener);
        temps = CollectUtil.query(title, link);
        if(temps.size() == 1) {
            fab.setImageResource(R.mipmap.ic_favorite_white_24dp);
        }
        htvContent = (HtmlTextView) findViewById(R.id.htv_content);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredGridLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NewsAdapter(this, mList);
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
        loadToast = new LoadToast(this);
        loadToast.setTranslationY(160);
        loadToast.setText("玩命加载中...");
        loadToast.show();
        if (link != null && !link.equals("")) {
            htvContent.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            new Thread(loadDetail).start();
        } else {
            htvContent.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
            new Thread(loadItem).start();
        }
    }

    public static void startActivity(Activity activity, int id, String title, String img, String link, ImageView ImageView) {
        Intent intent = new Intent();
        intent.setClass(activity, NewsDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("img", img);
        intent.putExtra("link", link);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, ImageView, Params.TRANSITION_PIC);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == fab) {
                if(temps.size() == 1) {
                    fab.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
                    CollectUtil.delete(temps.get(0));
                } else {
                    temp.setType(0);
                    temp.setCate(1);
                    temp.setCover(img);
                    temp.setTitle(title);
                    temp.setLink(link);
                    fab.setImageResource(R.mipmap.ic_favorite_white_24dp);
                    CollectUtil.add(temp);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        ivImg.destroyDrawingCache();
        htvContent.destroyDrawingCache();
        super.onDestroy();
    }

    @Override
    protected NewsPresenter onCreatePresenter() {
        return new NewsPresenter(this);
    }

    @Override
    public void showHotest(List<NewsBean> list) {

    }

    @Override
    public void showLatest(List<NewsBean> list) {

    }

    @Override
    public void showBefore(List<NewsBean> list) {

    }

    @Override
    public void showList(List<NewsBean> list) {

    }

    @Override
    public void showItem(List<NewsBean> list) {
        Message msg = new Message();
        msg.what = 4;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showDetail(NewsBean bean) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = bean;
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


    public static String getMD5(String val) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(val.getBytes());
        byte[] m = md5.digest();
        return getString(m);
    }

    private static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(b[i]);
        }
        return sb.toString();
    }
}
