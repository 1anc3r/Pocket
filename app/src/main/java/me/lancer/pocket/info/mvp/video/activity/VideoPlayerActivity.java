package me.lancer.pocket.info.mvp.video.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.mvp.base.activity.BaseActivity;
import me.lancer.pocket.util.DensityUtil;

public class VideoPlayerActivity extends BaseActivity {

    private WebView wvVideo;

    private int aid = 0;
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_video_player);
        initData();
        initView();
    }

    private void initData() {
        aid = getIntent().getIntExtra("aid", 0);
    }

    private void initView() {
        wvVideo = (WebView) findViewById(R.id.wv_video);
        wvVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) wvVideo.getLayoutParams();
        params.height = height + DensityUtil.dip2px(this, Float.parseFloat("80"));
        wvVideo.setLayoutParams(params);
        wvVideo.getSettings().setSupportZoom(true);
        wvVideo.getSettings().setBuiltInZoomControls(true);
        wvVideo.getSettings().setUseWideViewPort(true);
        wvVideo.getSettings().setJavaScriptEnabled(true);
        wvVideo.getSettings().setLoadWithOverviewMode(true);
        wvVideo.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wvVideo.getSettings().setLoadWithOverviewMode(true);
        wvVideo.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

        });
        videoUrl = "http://www.bilibili.com/video/av" + aid;
        wvVideo.loadUrl(videoUrl);
    }

    @Override
    protected void onDestroy() {
        destroy();
        super.onDestroy();
    }

    public void destroy() {
        if (wvVideo != null) {
            ViewParent parent = wvVideo.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(wvVideo);
            }
            wvVideo.stopLoading();
            wvVideo.getSettings().setJavaScriptEnabled(false);
            wvVideo.clearHistory();
            wvVideo.clearView();
            wvVideo.removeAllViews();
            try {
                wvVideo.destroy();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }
}
