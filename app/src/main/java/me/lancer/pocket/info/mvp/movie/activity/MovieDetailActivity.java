package me.lancer.pocket.info.mvp.movie.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.info.mvp.movie.IMovieView;
import me.lancer.pocket.info.mvp.movie.MovieBean;
import me.lancer.pocket.info.mvp.movie.MoviePresenter;
import me.lancer.pocket.ui.application.mParams;
import me.lancer.pocket.ui.view.htmltextview.HtmlHttpImageGetter;
import me.lancer.pocket.ui.view.htmltextview.HtmlTextView;

public class MovieDetailActivity extends PresenterActivity<MoviePresenter> implements IMovieView {

    private ImageView ivImg;
    private HtmlTextView htvInfo;
    private HtmlTextView htvContent;
    private LoadToast loadToast;

    private int type;
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
                    Log.e("log", (String) msg.obj);
                    loadToast.error();
                    break;
                case 3:
                    if (msg.obj != null) {
                        loadToast.success();
                        MovieBean bb = (MovieBean) msg.obj;
                        htvInfo.setHtml(bb.getSubTitle(), new HtmlHttpImageGetter(htvContent));
                        htvContent.setHtml(bb.getContent(), new HtmlHttpImageGetter(htvContent));
                    }
                    break;
            }
        }
    };

    private Runnable loadReviewerDetail = new Runnable() {
        @Override
        public void run() {
            presenter.loadReviewerDetail(link);
        }
    };

    private Runnable loadTopDetail = new Runnable() {
        @Override
        public void run() {
            presenter.loadTopDetail(link);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medimu);
        initData();
        initView();
    }

    private void initData() {
        type = getIntent().getIntExtra("type", 0);
        title = getIntent().getStringExtra("title");
        img = getIntent().getStringExtra("img");
        link = getIntent().getStringExtra("link");
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.t_large);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ivImg = (ImageView) findViewById(R.id.iv_img);
        ViewCompat.setTransitionName(ivImg, mParams.TRANSITION_PIC);
        Glide.with(this).load(img).into(ivImg);
        htvInfo = (HtmlTextView) findViewById(R.id.htv_info);
        htvContent = (HtmlTextView) findViewById(R.id.htv_content);
        loadToast = new LoadToast(this);
        loadToast.setTranslationY(160);
        loadToast.setText("玩命加载中...");
        loadToast.show();
        if (type == 0) {
            new Thread(loadTopDetail).start();
        } else if (type == 1) {
            new Thread(loadReviewerDetail).start();
        }
    }

    public static void startActivity(Activity activity, int type, String title, String img, String link, ImageView ImageView) {
        Intent intent = new Intent();
        intent.setClass(activity, MovieDetailActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("title", title);
        intent.putExtra("img", img);
        intent.putExtra("link", link);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, ImageView, mParams.TRANSITION_PIC);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onDestroy() {
        ivImg.destroyDrawingCache();
        htvInfo.destroyDrawingCache();
        htvContent.destroyDrawingCache();
        super.onDestroy();
    }

    @Override
    protected MoviePresenter onCreatePresenter() {
        return new MoviePresenter(this);
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
    public void showReviewer(List<MovieBean> list) {

    }

    @Override
    public void showTopMovie(List<MovieBean> list) {

    }

    @Override
    public void showReviewerDetail(MovieBean bean) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = bean;
        handler.sendMessage(msg);
    }

    @Override
    public void showTopDetail(MovieBean bean) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = bean;
        handler.sendMessage(msg);
    }
}
