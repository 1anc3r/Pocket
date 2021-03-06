package me.lancer.pocket.info.mvp.novel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.novel.INovelView;
import me.lancer.pocket.info.mvp.novel.NovelBean;
import me.lancer.pocket.info.mvp.novel.NovelPresenter;
import me.lancer.pocket.info.mvp.novel.adapter.ChapterAdapter;
import me.lancer.pocket.ui.application.Params;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;

/**
 * Created by HuangFangzhi on 2017/5/25.
 */

public class NovelDetailActivity extends PresenterActivity<NovelPresenter> implements INovelView {

    private Toolbar toolbar;
    private ImageView ivCover;
    private TextView tvTitle, tvAuthor, tvIntro, tvWordCount, tvFollowCount, tvRetentRatio;
    private LoadToast loadToast;
    private RecyclerView rvList;
    private ChapterAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private List<NovelBean.Chapters> list = new ArrayList<>();

    private String value1, value2, value4;
    private int value3;

    private Runnable loadDetail = new Runnable() {
        @Override
        public void run() {
            presenter.loadDetail(value1);
        }
    };

    private Handler handler = new Handler() {
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
                        NovelBean nb = (NovelBean) msg.obj;
                        ViewCompat.setTransitionName(ivCover, Params.TRANSITION_PIC);
                        Glide.with(NovelDetailActivity.this).load(nb.getCover()).into(ivCover);
                        tvTitle.setText(nb.getTitle());
                        tvAuthor.setText(nb.getAuthor());
                        tvIntro.setText(nb.getIntro());
                        tvWordCount.setText((nb.getCount() / 10000) + "万字");
                        tvFollowCount.setText(value3 + "人");
                        tvRetentRatio.setText(value4 + "%");
                    }
                    break;
                case 4:
                    if (msg.obj != null) {
                        loadToast.success();
                        list.clear();
                        list.addAll((List<NovelBean.Chapters>) msg.obj);
                        adapter = new ChapterAdapter(NovelDetailActivity.this, list);
                        rvList.setAdapter(adapter);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_info);
        initData();
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(value2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(value2);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ivCover = (ImageView) findViewById(R.id.iv_cover);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvAuthor = (TextView) findViewById(R.id.tv_author);
        tvIntro = (TextView) findViewById(R.id.tv_intro);
        tvWordCount = (TextView) findViewById(R.id.tv_word_count);
        tvFollowCount = (TextView) findViewById(R.id.tv_follow_count);
        tvRetentRatio = (TextView) findViewById(R.id.tv_retent_ratio);
        rvList = (RecyclerView) findViewById(R.id.rv_chapter);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        adapter = new ChapterAdapter(this, list);
        rvList.setAdapter(adapter);
        loadToast = new LoadToast(this);
        loadToast.setTranslationY(160);
        loadToast.setText("玩命加载中...");
        loadToast.show();
        new Thread(loadDetail).start();
    }

    private void initData() {
        value1 = getIntent().getStringExtra("value1");
        value2 = getIntent().getStringExtra("value2");
        value3 = getIntent().getIntExtra("value3", 0);
        value4 = getIntent().getStringExtra("value4");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_normal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                showAboutDialog();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("小说");
        builder.setMessage(
                "\t\t\t\t/*\n" +
                        "\t\t\t\t * 排行 : 小说排行榜\n" +
                        "\t\t\t\t * 分类 : 小说各分区\n" +
                        "\t\t\t\t * 搜索 : 点击右上角的搜索按钮搜索你想看的小说\n" +
                        "\t\t\t\t * ——数据来源 : 追书神器\n" +
                        "\t\t\t\t * （www.zhuishushenqi.com）\n" +
                        "\t\t\t\t */");
        builder.show();
    }

    public static void startActivity(Activity activity, String value1, String value2, int value3, String value4) {
        Intent intent = new Intent();
        intent.setClass(activity, NovelDetailActivity.class);
        intent.putExtra("value1", value1);
        intent.putExtra("value2", value2);
        intent.putExtra("value3", value3);
        intent.putExtra("value4", value4);
        ActivityCompat.startActivity(activity, intent, new Bundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            int position = resultCode;
            if (position == list.size()) {
                showSnackbar(rvList, "这是终章.");
            } else if (position == -1) {
                showSnackbar(rvList, "这是首章.");
            } else {
                NovelReadActivity.startActivity(this, position, list.get(position).getTitle(), list.get(position).getLink());
            }
        }
    }

    @Override
    protected NovelPresenter onCreatePresenter() {
        return new NovelPresenter(this);
    }

    @Override
    public void showRank(List<NovelBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showCate(List<NovelBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showSearch(List<NovelBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showDetail(NovelBean bean) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = bean;
        handler.sendMessage(msg);
    }

    @Override
    public void showChapter(List<NovelBean.Chapters> list) {
        Message msg = new Message();
        msg.what = 4;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showContent(String content) {

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
