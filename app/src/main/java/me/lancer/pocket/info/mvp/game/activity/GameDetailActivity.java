package me.lancer.pocket.info.mvp.game.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.game.GameBean;
import me.lancer.pocket.info.mvp.game.GamePresenter;
import me.lancer.pocket.info.mvp.game.IGameView;
import me.lancer.pocket.info.mvp.game.adapter.GameShotAdapter;
import me.lancer.pocket.ui.application.Params;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.ui.mvp.collect.CollectBean;
import me.lancer.pocket.ui.mvp.collect.CollectUtil;
import me.lancer.pocket.ui.view.htmltextview.HtmlHttpImageGetter;
import me.lancer.pocket.ui.view.htmltextview.HtmlTextView;

public class GameDetailActivity extends PresenterActivity<GamePresenter> implements IGameView {

    private ImageView ivCover;
    private TextView tvDiscount, tvOriginal, tvFinal, tvDevelopers, tvPublishers;
    private HtmlTextView htvLanguages, htvDescription, htvRequirements;
    private RecyclerView rvList;
    private GameShotAdapter adapter;
    private List<String> list = new ArrayList<>();
    private LoadToast loadToast;

    private FloatingActionButton fabCollect;
    private List<CollectBean> temps = new ArrayList<>();
    private CollectBean temp = new CollectBean();

    private String title, img, link;
    private int id;
    View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == fabCollect) {
                if (temps.size() == 1) {
                    fabCollect.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
                    CollectUtil.delete(temps.get(0));
                    temps = CollectUtil.query(title, String.valueOf(id));
                } else {
                    fabCollect.setImageResource(R.mipmap.ic_favorite_white_24dp);
                    temp.setType(2);
                    temp.setCate(13);
                    temp.setCover(img);
                    temp.setTitle(title);
                    temp.setLink(String.valueOf(id));
                    CollectUtil.add(temp);
                    temps = CollectUtil.query(title, String.valueOf(id));
                }
            }
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
                        loadToast.success();
                        GameBean bb = (GameBean) msg.obj;
                        tvDiscount.setText("-" + bb.getDiscountPercent() + "%");
                        tvOriginal.setText("￥" + bb.getOriginalPrice());
                        tvOriginal.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        tvFinal.setText("￥" + bb.getFinalPrice());
                        tvDevelopers.setText(bb.getDevelopers());
                        tvPublishers.setText(bb.getPublishers());
                        htvLanguages.setHtml(bb.getSupportedLanguages(), new HtmlHttpImageGetter(htvLanguages));
                        htvDescription.setHtml(bb.getDescription(), new HtmlHttpImageGetter(htvDescription));
                        htvRequirements.setHtml(bb.getRequirements(), new HtmlHttpImageGetter(htvRequirements));
                        list = bb.getScreenshots();
                        adapter = new GameShotAdapter(GameDetailActivity.this, list);
                        rvList.setAdapter(adapter);
                    }
                    break;
            }
        }
    };
    private Runnable loadDetail = new Runnable() {
        @Override
        public void run() {
            presenter.loadDetail(id);
        }
    };

    public static void startActivity(Activity activity, int id, String title, String img, ImageView ImageView) {
        Intent intent = new Intent();
        intent.setClass(activity, GameDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("img", img);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, ImageView, Params.TRANSITION_PIC);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initData();
        initView();
    }

    private void initData() {
        link = getIntent().getStringExtra("link");
        if (link == null) {
            id = getIntent().getIntExtra("id", 0);
        } else {
            id = Integer.parseInt(link);
        }
        title = getIntent().getStringExtra("title");
        img = getIntent().getStringExtra("img");
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
        fabCollect = (FloatingActionButton) findViewById(R.id.fab_collect);
        fabCollect.setOnClickListener(vOnClickListener);
        temps = CollectUtil.query(title, String.valueOf(id));
        if (temps.size() == 1) {
            fabCollect.setImageResource(R.mipmap.ic_favorite_white_24dp);
        } else {
            fabCollect.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
        }
        tvDiscount = (TextView) findViewById(R.id.tv_discount);
        tvOriginal = (TextView) findViewById(R.id.tv_original);
        tvFinal = (TextView) findViewById(R.id.tv_final);
        tvDevelopers = (TextView) findViewById(R.id.tv_developers);
        tvPublishers = (TextView) findViewById(R.id.tv_publishers);
        htvLanguages = (HtmlTextView) findViewById(R.id.htv_languages);
        htvDescription = (HtmlTextView) findViewById(R.id.htv_description);
        htvRequirements = (HtmlTextView) findViewById(R.id.htv_requirements);
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvList.setLayoutManager(llm);
        rvList.setItemAnimator(new DefaultItemAnimator());
        adapter = new GameShotAdapter(this, list);
        rvList.setAdapter(adapter);
        loadToast = new LoadToast(this);
        loadToast.setTranslationY(160);
        loadToast.setText("玩命加载中...");
        loadToast.show();
        new Thread(loadDetail).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ivCover.destroyDrawingCache();
        htvDescription.destroyDrawingCache();
        htvRequirements.destroyDrawingCache();
    }

    @Override
    protected GamePresenter onCreatePresenter() {
        return new GamePresenter(this);
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
    public void showFeatured(List<GameBean> list) {

    }

    @Override
    public void showCategories(List<GameBean> list) {

    }

    @Override
    public void showDetail(GameBean bean) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = bean;
        handler.sendMessage(msg);
    }
}
