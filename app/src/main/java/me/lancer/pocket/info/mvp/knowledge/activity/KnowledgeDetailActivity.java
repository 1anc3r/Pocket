package me.lancer.pocket.info.mvp.knowledge.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.info.mvp.knowledge.IKnowledgeView;
import me.lancer.pocket.info.mvp.knowledge.KnowledgeBean;
import me.lancer.pocket.info.mvp.knowledge.KnowledgePresenter;
import me.lancer.pocket.ui.application.mParams;
import me.lancer.pocket.ui.view.htmltextview.HtmlHttpImageGetter;
import me.lancer.pocket.ui.view.htmltextview.HtmlTextView;
import me.lancer.pocket.util.LruImageCache;

public class KnowledgeDetailActivity extends PresenterActivity<KnowledgePresenter> implements IKnowledgeView {

    private CollapsingToolbarLayout layout;
    private NetworkImageView ivImg;
    private HtmlTextView htvContent;
    private LoadToast loadToast;

    private RequestQueue mQueue;
    private int id, type;
    private String title, img;

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
                        KnowledgeBean mb = (KnowledgeBean) msg.obj;
                        layout.setTitle(mb.getTitle());
                        if (mb.getMessage() != null) {
                            htvContent.setHtml(mb.getMessage(), new HtmlHttpImageGetter(htvContent));
                        }
                    }
                    break;
            }
        }
    };

    private Runnable loadShow = new Runnable() {
        @Override
        public void run() {
            presenter.loadShow(type, id);
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
        mQueue = Volley.newRequestQueue(this);
        id = getIntent().getIntExtra("id", 0);
        title = getIntent().getStringExtra("title");
        img = getIntent().getStringExtra("img");
        type = getIntent().getIntExtra("type", 0);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.t_large);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        layout = (CollapsingToolbarLayout) findViewById(R.id.ctl_large);
        layout.setTitle(title);
        ivImg = (NetworkImageView) findViewById(R.id.iv_img);
        ViewCompat.setTransitionName(ivImg, mParams.TRANSITION_PIC);
        LruImageCache cache = LruImageCache.instance();
        ImageLoader loader = new ImageLoader(mQueue, cache);
        ivImg.setErrorImageResId(R.mipmap.ic_pictures_no);
        ivImg.setImageUrl(img, loader);
        htvContent = (HtmlTextView) findViewById(R.id.htv_content);
        loadToast = new LoadToast(this);
        loadToast.setTranslationY(160);
        loadToast.setText("玩命加载中...");
        loadToast.show();
        new Thread(loadShow).start();
    }

    public static void startActivity(Activity activity, int id, int type, String title, String img, ImageView networkImageView) {
        Intent intent = new Intent();
        intent.setClass(activity, KnowledgeDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        intent.putExtra("title", title);
        intent.putExtra("img", img);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, networkImageView, mParams.TRANSITION_PIC);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onDestroy() {
        ivImg.destroyDrawingCache();
        htvContent.destroyDrawingCache();
        super.onDestroy();
    }

    @Override
    protected KnowledgePresenter onCreatePresenter() {
        return new KnowledgePresenter(this);
    }

    @Override
    public void showClassify(List<KnowledgeBean> list) {

    }

    @Override
    public void showList(List<KnowledgeBean> list) {

    }

    @Override
    public void showNews(List<KnowledgeBean> list) {

    }

    @Override
    public void showShow(KnowledgeBean bean) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = bean;
        handler.sendMessage(msg);
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
