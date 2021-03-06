package me.lancer.pocket.info.mvp.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.code.CodeBean;
import me.lancer.pocket.info.mvp.code.CodePresenter;
import me.lancer.pocket.info.mvp.code.ICodeView;
import me.lancer.pocket.ui.application.Params;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;

public class CodeDetailActivity extends PresenterActivity<CodePresenter> implements ICodeView {

    private WebView wvContent;
    private LoadToast loadToast;

    private String title, img, link;

    private Runnable loadDetail = new Runnable() {
        @Override
        public void run() {
            presenter.loadDetail(link);
        }
    };

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
                        wvContent.loadUrl(link);
                        loadToast.success();
                    }
                    break;
            }
        }
    };

    public static void startActivity(Activity activity, String title, String star, String rank, String img, String link, ImageView ImageView) {
        Intent intent = new Intent();
        intent.setClass(activity, CodeDetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("star", star);
        intent.putExtra("rank", rank);
        intent.putExtra("img", img);
        intent.putExtra("link", link);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, ImageView, Params.TRANSITION_PIC);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public static void startActivity(Activity activity, String title, String star, String link) {
        Intent intent = new Intent();
        intent.setClass(activity, CodeDetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("star", star);
        intent.putExtra("link", link);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, null, Params.TRANSITION_PIC);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small);
        initData();
        initView();
    }

    private void initData() {
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
        wvContent = (WebView) findViewById(R.id.wv_content);
        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.requestFocus();
        wvContent.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        loadToast = new LoadToast(this);
        loadToast.setTranslationY(160);
        loadToast.setText("玩命加载中...");
        loadToast.show();
        new Thread(loadDetail).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected CodePresenter onCreatePresenter() {
        return new CodePresenter(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvContent.canGoBack()) {
            wvContent.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
    public void showUsers(List<CodeBean> list) {

    }

    @Override
    public void showOrganizations(List<CodeBean> list) {

    }

    @Override
    public void showRepositories(List<CodeBean> list) {

    }

    @Override
    public void showTrending(List<CodeBean> list) {

    }

    @Override
    public void showSearching(List<CodeBean> list) {

    }

    @Override
    public void showDetail(CodeBean bean) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = bean;
        handler.sendMessage(msg);
    }
}
