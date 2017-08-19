package me.lancer.pocket.info.mvp.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.app.AppBean;
import me.lancer.pocket.info.mvp.app.AppPresenter;
import me.lancer.pocket.info.mvp.app.IAppView;
import me.lancer.pocket.info.mvp.app.adapter.AppShotAdapter;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.ui.application.Params;
import me.lancer.pocket.ui.view.htmltextview.HtmlHttpImageGetter;
import me.lancer.pocket.ui.view.htmltextview.HtmlTextView;

public class AppDetailActivity extends PresenterActivity<AppPresenter> implements IAppView {

    private CollapsingToolbarLayout layout;
    private ImageView ivImg;
    private TextView tvVersNum, tvInfo;
    private HtmlTextView tvRemark, tvIntro, tvVersLog;
    private RatingBar rbStar;
    private Button btnDown;
    private RecyclerView rvList;
    private AppShotAdapter adapter;
    private List<String> shots = new ArrayList<>();

    private String id, title, img;
    private AppBean bean;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    if (msg.obj != null) {
                        bean = (AppBean) msg.obj;
                        tvVersNum.setText(bean.getVersNum() + "  " + bean.getSupport() + "+  " + bean.getLanguage());
                        tvInfo.setText(bean.getFavrNum() + getString(R.string.favr) + bean.getCommNum() + getString(R.string.comm) + bean.getDownNum() + getString(R.string.down));
                        tvVersLog.setText("\n" + bean.getVersNum() + "\n" + bean.getVersLog());
                        tvRemark.setText("\n" + bean.getRemark());
                        tvIntro.setHtml(getString(R.string.br) + bean.getIntro() + getString(R.string.br), new HtmlHttpImageGetter(tvIntro));
                        for (String itm : bean.getScreenshots()) {
                            shots.add(itm);
                        }
                        adapter.notifyDataSetChanged();
                        rbStar.setRating(Float.parseFloat(bean.getStar()));
                        btnDown.setText(bean.getDownNum() + getString(R.string.down));
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        initData();
        initView();
    }

    private void initData() {
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        img = getIntent().getStringExtra("img");
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        layout = (CollapsingToolbarLayout) findViewById(R.id.ctl);
        layout.setTitle(title);
        ivImg = (ImageView) findViewById(R.id.iv_cover);
        ViewCompat.setTransitionName(ivImg, Params.TRANSITION_PIC);
        Glide.with(this).load(img).into(ivImg);
        tvVersNum = (TextView) findViewById(R.id.tv_vers_num);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        tvVersLog = (HtmlTextView) findViewById(R.id.tv_vers_log);
        tvRemark = (HtmlTextView) findViewById(R.id.tv_remark);
        tvIntro = (HtmlTextView) findViewById(R.id.tv_intro);
        rbStar = (RatingBar) findViewById(R.id.rb_medimu);
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvList.setLayoutManager(llm);
        rvList.setItemAnimator(new DefaultItemAnimator());
        adapter = new AppShotAdapter(this, shots);
        rvList.setAdapter(adapter);
        btnDown = (Button) findViewById(R.id.btn_download);
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://www.coolapk.com/apk/" + bean.getPkgName());
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        new Thread(loadDetail).start();
    }

    public static void startActivity(Activity activity, String id, String title, String img, ImageView ImageView) {
        Intent intent = new Intent();
        intent.setClass(activity, AppDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("img", img);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, ImageView, Params.TRANSITION_PIC);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private File downFile(String httpUrl) {
        if (bean != null) {
            final String fileName = bean.getApkName() + ".apk";
            final String filePath = Environment.getExternalStorageDirectory().toString() + "/me.lancer.pocket.info";
            File tmpFile = new File(filePath);
            if (!tmpFile.exists()) {
                tmpFile.mkdir();
            }
            final File file = new File(filePath + fileName);
            try {
                URL url = new URL(httpUrl);
                try {
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    InputStream is = conn.getInputStream();
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buf = new byte[256];
                    conn.connect();
                    double count = 0;
                    if (conn.getResponseCode() >= 400) {
                        showSnackbar(btnDown, "连接超时!");
                    } else {
                        while (count <= 100) {
                            if (is != null) {
                                int numRead = is.read(buf);
                                if (numRead <= 0) {
                                    break;
                                } else {
                                    fos.write(buf, 0, numRead);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    conn.disconnect();
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return file;
        }
        return null;
    }

    private void openFile(File file) {
        if (file != null) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        ivImg.destroyDrawingCache();
        super.onDestroy();
    }

    @Override
    protected AppPresenter onCreatePresenter() {
        return new AppPresenter(this);
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
    public void showHomepage(List<AppBean> list) {

    }

    @Override
    public void showSearch(List<AppBean> list) {

    }

    @Override
    public void showDetail(AppBean bean) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = bean;
        handler.sendMessage(msg);
    }

    @Override
    public void showDownload(String log) {

    }

    @Override
    public void showUpgrade(List<AppBean> list) {

    }
}
