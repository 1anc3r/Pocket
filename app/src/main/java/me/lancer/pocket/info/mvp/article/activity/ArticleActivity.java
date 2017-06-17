package me.lancer.pocket.info.mvp.article.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.steamcrafted.loadtoast.LoadToast;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.article.ArticleBean;
import me.lancer.pocket.info.mvp.article.ArticlePresenter;
import me.lancer.pocket.info.mvp.article.IArticleView;
import me.lancer.pocket.info.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.ui.application.mParams;
import me.lancer.pocket.ui.view.htmltextview.HtmlHttpImageGetter;
import me.lancer.pocket.ui.view.htmltextview.HtmlTextView;

public class ArticleActivity extends PresenterActivity<ArticlePresenter> implements IArticleView {

    private FloatingActionButton fab;
    private CollapsingToolbarLayout layout;
    private ImageView ivImg;
    private HtmlTextView htvAuthor, htvContent;
    private LoadToast loadToast;

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
                    Log.e("log", (String) msg.obj);
                    loadToast.error();
                    break;
                case 3:
                    if (msg.obj != null) {
                        loadToast.success();
                        ArticleBean ab = (ArticleBean) msg.obj;
                        layout.setTitle(ab.getTitle());
                        if (ab.getContent() != null) {
                            htvAuthor.setHtml(ab.getAuthor(), new HtmlHttpImageGetter(htvAuthor));
                        }
                        if (ab.getContent() != null) {
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
        new Thread(loadDaily).start();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.t_large);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadToast.show();
                new Thread(loadRandom).start();
            }
        });
        ivImg = (ImageView) findViewById(R.id.iv_img);
        ViewCompat.setTransitionName(ivImg, mParams.TRANSITION_PIC);
        Glide.with(this).load("https://www.dujin.org/sys/bing/1366.php").into(ivImg);
        layout = (CollapsingToolbarLayout) findViewById(R.id.ctl_large);
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
