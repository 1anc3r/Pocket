package me.lancer.pocket.info.mvp.article.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.article.ArticleBean;
import me.lancer.pocket.info.mvp.article.ArticlePresenter;
import me.lancer.pocket.info.mvp.article.IArticleView;
import me.lancer.pocket.ui.application.Params;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.ui.mvp.collect.CollectBean;
import me.lancer.pocket.ui.mvp.collect.CollectUtil;
import me.lancer.pocket.ui.view.htmltextview.HtmlHttpImageGetter;
import me.lancer.pocket.ui.view.htmltextview.HtmlTextView;

public class ArticleActivity extends PresenterActivity<ArticlePresenter> implements IArticleView {

    private FloatingActionButton fabRefresh, fabCollect;
    private CollapsingToolbarLayout layout;
    private ImageView ivCover;
    private HtmlTextView htvAuthor, htvContent;
    private LoadToast loadToast;

    private List<CollectBean> temps = new ArrayList<>();
    private CollectBean temp = new CollectBean();

    private String title, author, content;

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
                        ArticleBean ab = (ArticleBean) msg.obj;
                        title = ab.getTitle();
                        layout.setTitle(title);
                        if (ab.getAuthor() != null) {
                            author = ab.getAuthor();
                            htvAuthor.setHtml(ab.getAuthor(), new HtmlHttpImageGetter(htvAuthor));
                        }
                        if (ab.getContent() != null) {
                            content = ab.getContent();
                            htvContent.setHtml(ab.getContent(), new HtmlHttpImageGetter(htvContent));
                        }
                    }
                    break;
            }
        }
    };

    private Runnable loadRandom = new Runnable() {
        @Override
        public void run() {
            presenter.loadRandom();
        }
    };

    private Runnable loadDaily = new Runnable() {
        @Override
        public void run() {
            presenter.loadDaily();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article);
        initView();
        initData();
    }

    private void initData() {
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        author = getIntent().getStringExtra("author");
        if (title == null && content == null) {
            new Thread(loadDaily).start();
        } else {
            layout.setTitle(title);
            htvAuthor.setHtml(author, new HtmlHttpImageGetter(htvAuthor));
            htvContent.setHtml(content, new HtmlHttpImageGetter(htvContent));
            loadToast.success();
        }
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        fabRefresh = (FloatingActionButton) findViewById(R.id.fab_refresh);
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadToast.show();
                new Thread(loadRandom).start();
            }
        });
        fabCollect = (FloatingActionButton) findViewById(R.id.fab_favorite);
        fabCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temps.size() == 1) {
                    fabCollect.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
                    CollectUtil.delete(temps.get(0));
                    temps = CollectUtil.query(title, author);
                } else {
                    fabCollect.setImageResource(R.mipmap.ic_favorite_white_24dp);
                    temp.setType(0);
                    temp.setCate(0);
                    temp.setCover(content);
                    temp.setTitle(title);
                    temp.setLink(author);
                    CollectUtil.add(temp);
                    temps = CollectUtil.query(title, author);
                }
            }
        });
        temps = CollectUtil.query(title, author);
        if (temps.size() == 1) {
            fabCollect.setImageResource(R.mipmap.ic_favorite_white_24dp);
        } else {
            fabCollect.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
        }
        ivCover = (ImageView) findViewById(R.id.iv_cover);
        ViewCompat.setTransitionName(ivCover, Params.TRANSITION_PIC);
        if ((Math.random() * 16) > 8) {
            Glide.with(this).load("https://raw.githubusercontent.com/1anc3r/Pocket/master/ic_day.png").into(ivCover);
        } else {
            Glide.with(this).load("https://raw.githubusercontent.com/1anc3r/Pocket/master/ic_night.png").into(ivCover);
        }
        layout = (CollapsingToolbarLayout) findViewById(R.id.ctl);
        htvAuthor = (HtmlTextView) findViewById(R.id.htv_author);
        htvContent = (HtmlTextView) findViewById(R.id.htv_content);
        loadToast = new LoadToast(this);
        loadToast.setTranslationY(160);
        loadToast.setText("玩命加载中...");
        loadToast.show();
    }

    @Override
    protected void onDestroy() {
        htvAuthor.destroyDrawingCache();
        htvContent.destroyDrawingCache();
        super.onDestroy();
    }

    @Override
    protected ArticlePresenter onCreatePresenter() {
        return new ArticlePresenter(this);
    }

    @Override
    public void showDaily(ArticleBean bean) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = bean;
        handler.sendMessage(msg);
    }

    @Override
    public void showRandom(ArticleBean bean) {
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
}
