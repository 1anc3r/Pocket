package me.lancer.pocket.mainui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationMode;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.polaric.colorful.ColorPickerDialog;
import org.polaric.colorful.Colorful;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.base.activity.BaseActivity;
import me.lancer.pocket.info.mvp.repository.RepositoryBean;
import me.lancer.pocket.info.mvp.repository.adapter.RepositoryAdapter;
import me.lancer.pocket.mainui.adapter.QAAdapter;
import me.lancer.pocket.mainui.adapter.SettingAdapter;
import me.lancer.pocket.mainui.application.App;
import me.lancer.pocket.mainui.application.Params;
import me.lancer.pocket.util.ContentGetterSetter;

public class SettingActivity extends BaseActivity {

    private App app;

    private Toolbar toolbar;
    private LinearLayout llNight, llBright, llTheme, llFunc, llProblem, llUpdate, llFeedback, llDownload, llAboutUs;
    private Button btnLoginOut;
    private SwitchCompat scNight;
    private BottomSheetDialog listDialog;
    private AlertDialog aboutDialog;
    private ProgressDialog progressDialog;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int screenMode;
    private int screenBrightness;

    private List<String> funcList = new ArrayList<>(), problemList = new ArrayList<>(), logList = new ArrayList<>();
    private List<RepositoryBean> reList = new ArrayList<>();
    ContentGetterSetter contentGetterSetter = new ContentGetterSetter();
    private final String root = Environment.getExternalStorageDirectory() + "/";

    private Runnable repository = new Runnable() {
        @Override
        public void run() {
            String content = contentGetterSetter.getContentFromHtml("repository", "https://raw.githubusercontent.com/1anc3r/1anc3r-s-Programming-Journey/master/AppLink.md");
            if (!content.contains("获取失败!")) {
                try {
                    List<RepositoryBean> list = new ArrayList<>();
                    JSONObject jsonObj = new JSONObject(content);
                    JSONArray jsonArr = jsonObj.getJSONArray("apps");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        RepositoryBean bean = new RepositoryBean();
                        JSONObject jsonItem = jsonArr.getJSONObject(i);
                        bean.setImg(jsonItem.getString("img"));
                        bean.setName(jsonItem.getString("name"));
                        bean.setDescription(jsonItem.getString("description"));
                        bean.setDownload(jsonItem.getString("download"));
                        bean.setBlog(jsonItem.getString("blog"));
                        list.add(bean);
                    }
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = list;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Runnable update = new Runnable() {
        @Override
        public void run() {
            String content = contentGetterSetter.getContentFromHtml("repository", "https://raw.githubusercontent.com/1anc3r/Pocket/master/app/src/main/assets/updates.json");
            if (!content.contains("获取失败!")) {
                try {
                    List<String> list = new ArrayList<>();
                    JSONObject jsonObj = new JSONObject(content);
                    JSONArray jsonArr = jsonObj.getJSONArray("updates");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        String item = "";
                        JSONObject jsonItem = jsonArr.getJSONObject(i);
                        item = item + "版本 : " + jsonItem.getString("version") + "\n";
                        item = item + "内容 : \n" + jsonItem.getString("log");
                        list.add(item.replace("；", "；\n"));
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = list;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (msg.obj != null) {
                        reList = (List<RepositoryBean>) msg.obj;
                        showRepositoryDialog(reList);
                        progressDialog.dismiss();
                    }
                    break;
                case 1:
                    if (msg.obj != null) {
                        logList = (List<String>) msg.obj;
                        showUpdateDialog(logList);
                        progressDialog.dismiss();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar("设置");
        llNight = (LinearLayout) findViewById(R.id.ll_night);
        llNight.setOnClickListener(vOnClickListener);
        llTheme = (LinearLayout) findViewById(R.id.ll_theme);
        llTheme.setOnClickListener(vOnClickListener);
        llBright = (LinearLayout) findViewById(R.id.ll_bright);
        llBright.setOnClickListener(vOnClickListener);
        llFunc = (LinearLayout) findViewById(R.id.ll_func);
        llFunc.setOnClickListener(vOnClickListener);
        llProblem = (LinearLayout) findViewById(R.id.ll_problem);
        llProblem.setOnClickListener(vOnClickListener);
        llUpdate = (LinearLayout) findViewById(R.id.ll_update);
        llUpdate.setOnClickListener(vOnClickListener);
        llFeedback = (LinearLayout) findViewById(R.id.ll_feedback);
        llFeedback.setOnClickListener(vOnClickListener);
        llDownload = (LinearLayout) findViewById(R.id.ll_download);
        llDownload.setOnClickListener(vOnClickListener);
        llAboutUs = (LinearLayout) findViewById(R.id.ll_about_us);
        llAboutUs.setOnClickListener(vOnClickListener);
        scNight = (SwitchCompat) findViewById(R.id.sc_night);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载,请稍后...");
        progressDialog.setCancelable(true);
        showAboutDialog();
    }

    private void initData() {
        app = (App) this.getApplication();
        sharedPreferences = this.getSharedPreferences(getString(R.string.spf_user), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
//        night = sharedPreferences.getBoolean(Params.ISNIGHT, false);
        scNight.setChecked(app.isNight());
        scNight.setClickable(false);
        funcList.add("— 工具 —");
        funcList.add("电话、通讯录、信息 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 电话 : 列出手机上的通话记录\n" +
                "\t\t\t\t * 通讯录 : 列出手机上的联系人\n" +
                "\t\t\t\t * 信息 : 列出手机上的短信记录\n" +
                "\t\t\t\t */");
        funcList.add("图片、音乐、视频、文档、应用、存储 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 列出手机上的各种类型的文件\n" +
                "\t\t\t\t * 支持打开、删除、赋值、剪切\n" +
                "\t\t\t\t */");
        funcList.add("日历 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 一个普通的日历\n" +
                "\t\t\t\t * 需要手动添加事项\n" +
                "\t\t\t\t */");
//        funcList.add("时钟 : \n" +
//                "\t\t\t\t正在卖力开发中...");
//        funcList.add("备忘录 : \n" +
//                "\t\t\t\t正在卖力开发中...");
        funcList.add("天气 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 天气信息 : 提供全国各城市的天气信息\n" +
                "\t\t\t\t * 城市选择 : 然而并不能定位\n" +
                "\t\t\t\t * ——数据来源 : 中央天气\n" +
                "\t\t\t\t * （tj.nineton.cn/Heart/index）\n" +
                "\t\t\t\t */");
        funcList.add("翻译 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * ——翻译支持 : 必应词典\n" +
                "\t\t\t\t * （cn.bing.com/dict/）\n" +
                "\t\t\t\t */");
        funcList.add("摩斯电码 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 在字符串和摩斯电码之间任意转换\n" +
                "\t\t\t\t * 长按粘贴, 双击复制\n" +
                "\t\t\t\t */");
        funcList.add("计算器 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 这是一个支持多项式运算的计算器, 由栈实现\n" +
                "\t\t\t\t */");
        funcList.add("二维码 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 扫描 : 调用后置摄像头扫描二维码\n" +
                "\t\t\t\t * 识别 : 识别本机图片中的二维码\n" +
                "\t\t\t\t * ——使用开源库 : （github.com/scola/Qart）\n" +
                "\t\t\t\t */");
        funcList.add("— 资讯 —");
        funcList.add("文章 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 每日一文 : 每天一篇精选优质短篇\n" +
                "\t\t\t\t * 随机文章 : 点击刷新按钮随机读文章\n" +
                "\t\t\t\t * ——数据来源 : " +
                "\n\t\t\t\t *  每日一文（meiriyiwen.com）" +
                "\n\t\t\t\t *  背景图片（bing.ioliu.cn）\n" +
                "\t\t\t\t */");
        funcList.add("趣闻 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 每日 : 知乎日报的每日信息\n" +
                "\t\t\t\t * 热门 : 知乎日报的热门信息\n" +
                "\t\t\t\t * 分类 : 包括动漫、游戏、财经、\n" +
                "\t\t\t\t *        电影、音乐、互联网安全等日报\n" +
                "\t\t\t\t * ——数据来源 : 知乎日报\n" +
                "\t\t\t\t * （news-at.zhihu.com/api）\n" +
                "\t\t\t\t */");
        funcList.add("段子 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 段子 : 内涵段子的热辣段子\n" +
                "\t\t\t\t * 图片 : 内涵段子的爆笑图片\n" +
                "\t\t\t\t * ——数据来源 : 内涵段子\n" +
                "\t\t\t\t * （neihanshequ.com）\n" +
                "\t\t\t\t */");
        funcList.add("图书 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 书评 : 豆瓣读书的最受欢迎书评\n" +
                "\t\t\t\t * 书榜 : 爬取呈现豆瓣图书TOP250\n" +
                "\t\t\t\t * 搜索 : 点击右上角的搜索按钮\n" +
                "\t\t\t\t *        搜索你想了解的图书信息\n" +
                "\t\t\t\t * ——数据来源 : 豆瓣读书\n" +
                "\t\t\t\t * （book.douban.com）\n" +
                "\t\t\t\t */");
        funcList.add("音乐 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 乐评 : 豆瓣音乐的最受欢迎乐评\n" +
                "\t\t\t\t * 乐榜 : 爬取呈现豆瓣音乐TOP250\n" +
                "\t\t\t\t * 搜索 : 点击右上角的搜索按钮\n" +
                "\t\t\t\t *        搜索你想了解的音乐信息\n" +
                "\t\t\t\t * ——数据来源 : 豆瓣音乐\n" +
                "\t\t\t\t * （music.douban.com）\n" +
                "\t\t\t\t */");
        funcList.add("电影 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 影评 : 豆瓣电影的最受欢迎影评\n" +
                "\t\t\t\t * 影榜 : 爬取呈现豆瓣电影TOP250\n" +
                "\t\t\t\t * 搜索 : 点击右上角的搜索按钮\n" +
                "\t\t\t\t *        搜索你想了解的电影信息\n" +
                "\t\t\t\t * ——数据来源 : 豆瓣电影\n" +
                "\t\t\t\t * （movie.douban.com）\n" +
                "\t\t\t\t */");
        funcList.add("图片 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 妹子 : 好看的妹子图\n" +
                "\t\t\t\t * 美景 : 好看的风景照\n" +
                "\t\t\t\t * ——数据来源 : " +
                "\n\t\t\t\t *  佳人 : Gank.io（gank.io）" +
                "\n\t\t\t\t *  美图 : Pexels（www.pexels.com）\n" +
                "\t\t\t\t */");
        funcList.add("小说 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 排行 : 小说排行榜\n" +
                "\t\t\t\t * 分类 : 小说各分区\n" +
                "\t\t\t\t * 搜索 : 点击右上角的搜索按钮搜索你想看的小说\n" +
                "\t\t\t\t * ——数据来源 : 追书神器\n" +
                "\t\t\t\t * （www.zhuishushenqi.com）\n" +
                "\t\t\t\t */");
        funcList.add("漫画 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 推荐 : 推荐好看的漫画\n" +
                "\t\t\t\t * 排行 : 漫画排行榜\n" +
                "\t\t\t\t * 分类 : 来自有妖气各分区排行榜\n" +
                "\t\t\t\t * 搜索 : 点击右上角的搜索按钮搜索你想看的漫画\n" +
                "\t\t\t\t * ——数据来源 : 有妖气（www.u17.com）\n" +
                "\t\t\t\t */");
        funcList.add("视频 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 分类 : 来自B站各分区排行榜前十\n" +
                "\t\t\t\t * ——数据来源 : BiliBili（api.bilibili.com）\n" +
                "\t\t\t\t */");
        funcList.add("游戏 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 精选 : 精选各大平台热门游戏\n" +
                "\t\t\t\t * 优惠、热销、新品、即将推出\n" +
                "\t\t\t\t * ——数据来源 : Steam\n" +
                "\t\t\t\t * （store.steampowered.com）\n" +
                "\t\t\t\t */");
        funcList.add("编程 : \n" +
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 个人 : GitHub上Star最多的个人\n" +
                "\t\t\t\t * 组织 : GitHub上Star最多的组织\n" +
                "\t\t\t\t * 项目 : GitHub上Star最多的项目\n" +
                "\t\t\t\t * 趋势 : GitHub上今日最热的项目\n" +
                "\t\t\t\t * 搜索 : 点击右上角的搜索按钮\n" +
                "\t\t\t\t *        搜索你想浏览的项目\n" +
                "\t\t\t\t * ——数据来源 : GithubRanking\n" +
                "\t\t\t\t * （github-ranking.com）\n" +
                "\t\t\t\t */");
        problemList.add("Q : 主页上的搜索栏好像并没有什么用...");
        problemList.add("A : 因为模块比较多, 各模块的搜索接口还没有统一, 个别模块中有搜索功能, 下一个版本搜索功能会上线. 如果您对搜索功能有什么好的建议或意见请通过意见反馈通道或者发送邮件联系1anc3r. ");
        problemList.add("Q : 为什么 \"信息\" 列表不显示联系人的名字?");
        problemList.add("A : 因为1anc3r太懒了, 没有将 \"信息\" 列表和 \"联系人\" 列表进行比对╮(╯_╰)╭. ");
        problemList.add("Q : 为什么发送了短信却没有记录?");
        problemList.add("A : 1anc3r在虚拟机上测试时没问题, 但是真机测试时也无法写入数据库, 目前正在努力寻找解决方法. ");
        problemList.add("Q : 为什么 \"文档\" 和 \"应用\" 加载时间很久?");
        problemList.add("A : Android没有提供 \"文档\" 的访问接口, 1anc3r是通过遍历文件系统来识别文档的(๑•́ ₃•̀๑); " +
                " \"应用\" 加载时间长是因为您的手机安装的应用太多了, 第二次加载会快一丢丢(ง •̀_•́)ง. ");
        problemList.add("Q : \"天气\" 中选择的城市没有数据怎么办, 而且别的天气应用都可以定位城市, 为什么你的 \"天气\" 没办法定位城市?");
        problemList.add("A : \"天气\" 数据来自中央天气, 没有数据的话1anc3r也造不出来. 没有加入定位的原因是中央天气的城市代码和高德地图的城市代码不一样. ");
        problemList.add("Q : \"资讯\" 中数据加载时间长或者加载不出来怎么办?");
        problemList.add("A : 首先, 检查您的网络是否可用; 其次, 下拉刷新可以解决一部分问题.\n" +
                "(Ps. \"游戏\" 数据来自Steam, \"编程\" 数据来自Github, 这俩货访问速度肯定比其他国内站点慢)");
        problemList.add("Q : 以后还有什么新功能模块?");
        problemList.add("A : 目前1anc3r的想法是加入日历、时钟、备忘录、收藏、通话短信统计. 如果你有什么有趣的API的话不妨联系1anc3r.");
        problemList.add("Q : 1anc3r, 1anc3r, 你的程序又崩溃了!");
        problemList.add("A : 方法一, 通过 \"意见反馈\" 撩一撩1anc3r; 方法二, 发送邮件至huangfangzhi0@foxmail.com");
    }

    private View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == llNight) {
                switchLight();
            } else if (v == llBright) {
                showBrightSeekDialog();
            } else if (v == llTheme) {
                showColorPickDialog();
            } else if (v == llFunc) {
                showListDialog(1, funcList);
            } else if (v == llProblem) {
                showListDialog(2, problemList);
            } else if (v == llUpdate) {
                new Thread(update).start();
            } else if (v == llFeedback) {
                Instabug.invoke(InstabugInvocationMode.NEW_CHAT);
            } else if (v == llDownload) {
                new Thread(repository).start();
                progressDialog.show();
            } else if (v == llAboutUs) {
                aboutDialog.show();
            }
        }
    };

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
            scNight.setChecked(true);
            app.setNight(true);
            editor.putBoolean(Params.ISNIGHT, true);
            editor.apply();
            Colorful.config(SettingActivity.this)
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
            scNight.setChecked(false);
            app.setNight(false);
            editor.putBoolean(Params.ISNIGHT, false);
            editor.apply();
            Colorful.config(SettingActivity.this)
                    .primaryColor(Colorful.ThemeColor.RED)
                    .accentColor(Colorful.ThemeColor.RED)
                    .translucent(false)
                    .dark(false)
                    .apply();
            recreate();
        }
    }

    private void showBrightSeekDialog() {
        LayoutInflater inflater = LayoutInflater.from(SettingActivity.this);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_seekbar_view, null);
        final Dialog dialog = new AlertDialog.Builder(SettingActivity.this).create();
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

    private void showColorPickDialog() {
        ColorPickerDialog dialog = new ColorPickerDialog(SettingActivity.this);
        dialog.setTitle("主题换色");
        dialog.setOnColorSelectedListener(new ColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(Colorful.ThemeColor themeColor) {
                if (themeColor.getColorRes() == R.color.md_deep_orange_500) {
                    app.setNight(true);
                    editor.putBoolean(Params.ISNIGHT, true);
                    editor.apply();
                } else {
                    app.setNight(false);
                    editor.putBoolean(Params.ISNIGHT, false);
                    editor.apply();
                }
                Colorful.config(SettingActivity.this)
                        .primaryColor(themeColor)
                        .accentColor(themeColor)
                        .translucent(false)
                        .dark(app.isNight())
                        .apply();
                recreate();
            }
        });
        dialog.show();
    }

    public void showListDialog(int type, List<String> list) {
        if (type == 1) {
            View listDialogView = View.inflate(this, R.layout.dialog_list, null);
            TextView tvType = (TextView) listDialogView.findViewById(R.id.tv_type);
            tvType.setText("功能介绍");
            RecyclerView rvList = (RecyclerView) listDialogView.findViewById(R.id.rv_list);
            rvList.setItemAnimator(new DefaultItemAnimator());
            rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            RecyclerView.Adapter adapter = new SettingAdapter(this, list);
            rvList.setAdapter(adapter);
            listDialog = new BottomSheetDialog(this);
            listDialog.setContentView(listDialogView);
            listDialog.show();
        } else if (type == 2) {
            View listDialogView = View.inflate(this, R.layout.dialog_list, null);
            TextView tvType = (TextView) listDialogView.findViewById(R.id.tv_type);
            tvType.setText("常见问题");
            RecyclerView rvList = (RecyclerView) listDialogView.findViewById(R.id.rv_list);
            rvList.setItemAnimator(new DefaultItemAnimator());
            rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            QAAdapter adapter = new QAAdapter(this, list);
            rvList.setAdapter(adapter);
            listDialog = new BottomSheetDialog(this);
            listDialog.setContentView(listDialogView);
            listDialog.show();
        }
    }

    private void showAboutDialog() {
        View aboutDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_about, null);
        TextView tvOrganization = (TextView) aboutDialogView.findViewById(R.id.tv_organization);
        tvOrganization.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://www.xiyoumobile.com/");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        TextView tvBlog = (TextView) aboutDialogView.findViewById(R.id.tv_blog);
        tvBlog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://www.1anc3r.me");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        TextView tvShare = (TextView) aboutDialogView.findViewById(R.id.tv_share);
        tvShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
//                intent.putExtra(Intent.EXTRA_TEXT, "看看我发现了什么宝贝(ง •̀_•́)ง\nhttps://www.coolapk.com/apk/me.lancer.pocket" + "\n分享自口袋");
//                startActivity(Intent.createChooser(intent, "分享到"));
                onClickShare("口袋", "快看看我发现了什么宝贝(ง •̀_•́)ง\n——分享自@口袋", "https://www.coolapk.com/apk/me.lancer.pocket", "http://image.coolapk.com/apk_logo/2017/0715/ic_launcher-web-for-145716-o_1bl3b1tcc4fnrri1gtpm50137q14-uid-659349.png", "口袋");
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(aboutDialogView);
        aboutDialog = builder.create();
    }

    private void showRepositoryDialog(List<RepositoryBean> list) {
        View listDialogView = View.inflate(this, R.layout.dialog_list, null);
        TextView tvType = (TextView) listDialogView.findViewById(R.id.tv_type);
        tvType.setText("相关应用");
        RecyclerView rvList = (RecyclerView) listDialogView.findViewById(R.id.rv_list);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RecyclerView.Adapter adapter = new RepositoryAdapter(this, list);
        rvList.setAdapter(adapter);
        listDialog = new BottomSheetDialog(this);
        listDialog.setContentView(listDialogView);
        listDialog.show();
    }

    private void showUpdateDialog(List<String> logList) {
        View listDialogView = View.inflate(this, R.layout.dialog_list, null);
        TextView tvType = (TextView) listDialogView.findViewById(R.id.tv_type);
        tvType.setText("更新日志");
        RecyclerView rvList = (RecyclerView) listDialogView.findViewById(R.id.rv_list);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RecyclerView.Adapter adapter = new SettingAdapter(this, logList);
        rvList.setAdapter(adapter);
        listDialog = new BottomSheetDialog(this);
        listDialog.setContentView(listDialogView);
        listDialog.show();
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
}
