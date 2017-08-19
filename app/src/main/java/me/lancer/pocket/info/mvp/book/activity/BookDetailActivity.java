package me.lancer.pocket.info.mvp.book.activity;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.book.BookBean;
import me.lancer.pocket.info.mvp.book.BookPresenter;
import me.lancer.pocket.info.mvp.book.IBookView;
import me.lancer.pocket.ui.application.Params;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.ui.mvp.collect.CollectBean;
import me.lancer.pocket.ui.mvp.collect.CollectUtil;
import me.lancer.pocket.ui.view.htmltextview.HtmlHttpImageGetter;
import me.lancer.pocket.ui.view.htmltextview.HtmlTextView;

public class BookDetailActivity extends PresenterActivity<BookPresenter> implements IBookView {

    private ImageView ivCover;
    private HtmlTextView htvInfo;
    private HtmlTextView htvContent;
    private LoadToast loadToast;

    private List<CollectBean> temps = new ArrayList<>();
    private CollectBean temp = new CollectBean();

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
                    loadToast.error();
                    break;
                case 3:
                    if (msg.obj != null) {
                        loadToast.success();
                        BookBean bb = (BookBean) msg.obj;
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

    public static void startActivity(Activity activity, int type, String title, String img, String link, ImageView ImageView) {
        Intent intent = new Intent();
        intent.setClass(activity, BookDetailActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("title", title);
        intent.putExtra("img", img);
        intent.putExtra("link", link);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, ImageView, Params.TRANSITION_PIC);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ivCover = (ImageView) findViewById(R.id.iv_cover);
        ViewCompat.setTransitionName(ivCover, Params.TRANSITION_PIC);
        Glide.with(this).load(img).into(ivCover);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_collect_item, menu);
        MenuItem item = menu.findItem(R.id.menu_favorite);
        temps = CollectUtil.query(title, link);
        if (temps.size() == 1) {
            item.setIcon(R.mipmap.ic_favorite_white_24dp);
        } else {
            item.setIcon(R.mipmap.ic_favorite_border_white_24dp);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorite:
                item.setIcon(R.mipmap.ic_favorite_white_24dp);
                if (temps.size() == 1) {
                    item.setIcon(R.mipmap.ic_favorite_border_white_24dp);
                    CollectUtil.delete(temps.get(0));
                    temps = CollectUtil.query(title, link);
                } else {
                    item.setIcon(R.mipmap.ic_favorite_white_24dp);
                    temp.setType(0);
                    temp.setCate(type + 3);
                    temp.setCover(img);
                    temp.setTitle(title);
                    temp.setLink(link);
                    CollectUtil.add(temp);
                    temps = CollectUtil.query(title, link);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ivCover.destroyDrawingCache();
        htvInfo.destroyDrawingCache();
        htvContent.destroyDrawingCache();
    }

    @Override
    protected BookPresenter onCreatePresenter() {
        return new BookPresenter(this);
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
    public void showReviewer(List<BookBean> list) {

    }

    @Override
    public void showTopBook(List<BookBean> list) {

    }

    @Override
    public void showReviewerDetail(BookBean bean) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = bean;
        handler.sendMessage(msg);
    }

    @Override
    public void showTopDetail(BookBean bean) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = bean;
        handler.sendMessage(msg);
    }
}
