package me.lancer.pocket.info.mvp.novel.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import net.steamcrafted.loadtoast.LoadToast;

import org.polaric.colorful.ColorPickerDialog;
import org.polaric.colorful.Colorful;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.novel.INovelView;
import me.lancer.pocket.info.mvp.novel.NovelBean;
import me.lancer.pocket.info.mvp.novel.NovelPresenter;
import me.lancer.pocket.ui.application.App;
import me.lancer.pocket.ui.application.Params;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;

/**
 * Created by HuangFangzhi on 2017/5/25.
 */

public class NovelReadActivity extends PresenterActivity<NovelPresenter> implements INovelView {

    Toolbar toolbar;
    private App app;
    private LoadToast loadToast;
    private TextView tvContent, tvPrev, tvNext;

    private int position, screenMode, screenBrightness;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean night = false;
    private String value1, value2;

    private Runnable loadDetail = new Runnable() {
        @Override
        public void run() {
            presenter.loadContent(value2);
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
                        tvContent.setText((String) msg.obj);
                    }
                    break;
            }
        }
    };

    private View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            if (view == tvPrev) {
                setResult(position - 1, intent);
            } else if (view == tvNext) {
                setResult(position + 1, intent);
            }
            intent.setClass(NovelReadActivity.this, NovelDetailActivity.class);
            finish();
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    };

    public static void startActivity(Activity activity, int position, String value1, String value2) {
        Intent intent = new Intent();
        intent.setClass(activity, NovelReadActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("value1", value1);
        intent.putExtra("value2", value2);
        activity.startActivityForResult(intent, position, new Bundle());
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_read);
        initData();
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(value1);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvPrev = (TextView) findViewById(R.id.tv_prev);
        tvPrev.setOnClickListener(vOnClickListener);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvNext.setOnClickListener(vOnClickListener);
        loadToast = new LoadToast(this);
        loadToast.setTranslationY(160);
        loadToast.setText("玩命加载中...");
        loadToast.show();
        new Thread(loadDetail).start();
    }

    private void initData() {
        app = (App) this.getApplication();
        sharedPreferences = this.getSharedPreferences(getString(R.string.spf_user), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        night = sharedPreferences.getBoolean(Params.ISNIGHT, false);

        position = getIntent().getIntExtra("position", 0);
        value1 = getIntent().getStringExtra("value1");
        value2 = getIntent().getStringExtra("value2");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_novel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                showAboutDialog();
                break;
            case R.id.menu_theme:
                switchLight();
                break;
            case R.id.menu_bright:
                showBrightDialog();
                break;
            case android.R.id.home:
                Intent intent = new Intent();
                intent.setClass(NovelReadActivity.this, NovelDetailActivity.class);
                setResult(-1, intent);
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClass(NovelReadActivity.this, NovelDetailActivity.class);
            setResult(-1, intent);
            finish();
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
        return super.onKeyDown(keyCode, event);
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

    private void switchLight() {
        if (!app.isNight()) {
            try {
                screenMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
                screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                if (screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                    setScreenMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                }
                setScreenBrightness(255.0F / 4);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            app.setNight(true);
            editor.putBoolean(Params.ISNIGHT, true);
            editor.apply();
            Colorful.config(NovelReadActivity.this)
                    .primaryColor(Colorful.ThemeColor.DEEP_ORANGE)
                    .accentColor(Colorful.ThemeColor.DEEP_ORANGE)
                    .translucent(false)
                    .dark(true)
                    .apply();
            recreate();
        } else {
            try {
                screenMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
                screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                if (screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                    setScreenMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                }
                setScreenBrightness(255.0F);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            app.setNight(false);
            editor.putBoolean(Params.ISNIGHT, false);
            editor.apply();
            Colorful.config(NovelReadActivity.this)
                    .primaryColor(Colorful.ThemeColor.RED)
                    .accentColor(Colorful.ThemeColor.RED)
                    .translucent(false)
                    .dark(false)
                    .apply();
            recreate();
        }
    }

    private void showColorPickDialog() {
        ColorPickerDialog dialog = new ColorPickerDialog(NovelReadActivity.this);
        dialog.setTitle("切换主题");
        dialog.setOnColorSelectedListener(new ColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(Colorful.ThemeColor themeColor) {
                night = themeColor.getColorRes() == R.color.md_deep_orange_500;
                Colorful.config(NovelReadActivity.this)
                        .primaryColor(themeColor)
                        .accentColor(themeColor)
                        .translucent(false)
                        .dark(night)
                        .apply();
                recreate();
            }
        });
        dialog.show();
    }

    private void showBrightDialog() {
        LayoutInflater inflater = LayoutInflater.from(NovelReadActivity.this);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_seekbar, null);
        final Dialog dialog = new AlertDialog.Builder(NovelReadActivity.this).create();
        try {
            screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        SeekBar sbBright = (SeekBar) layout.findViewById(R.id.sb_vorb);
        sbBright.setProgress(screenBrightness);
        sbBright.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setScreenBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        dialog.show();
        dialog.getWindow().setContentView(layout);
    }

    private void setScreenMode(int value) {
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, value);
    }

    private void setScreenBrightness(float value) {
        Window mWindow = getWindow();
        WindowManager.LayoutParams mParams = mWindow.getAttributes();
        float f = value / 255.0F;
        mParams.screenBrightness = f;
        mWindow.setAttributes(mParams);
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) value);
    }

    @Override
    protected NovelPresenter onCreatePresenter() {
        return new NovelPresenter(this);
    }

    @Override
    public void showRank(List<NovelBean> list) {

    }

    @Override
    public void showCate(List<NovelBean> list) {

    }

    @Override
    public void showSearch(List<NovelBean> list) {

    }

    @Override
    public void showDetail(NovelBean bean) {

    }

    @Override
    public void showChapter(List<NovelBean.Chapters> list) {

    }

    @Override
    public void showContent(String content) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = content;
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
