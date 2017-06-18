package me.lancer.pocket.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.polaric.colorful.ColorPickerDialog;
import org.polaric.colorful.Colorful;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.repository.RepositoryBean;
import me.lancer.pocket.info.mvp.repository.adapter.RepositoryAdapter;
import me.lancer.pocket.ui.activity.MainActivity;
import me.lancer.pocket.ui.activity.SettingActivity;
import me.lancer.pocket.ui.adapter.SettingAdapter;
import me.lancer.pocket.ui.application.mApp;
import me.lancer.pocket.ui.application.mParams;
import me.lancer.pocket.util.ContentGetterSetter;

/**
 * Created by HuangFangzhi on 2017/6/18.
 */

public class SettingFragment extends Fragment {

    private mApp app;

    private LinearLayout llNight, llTheme, llFunc, llProblem, llFeedback, llDownload, llAboutUs;
    private SwitchCompat scNight;
    private BottomSheetDialog listDialog;
    private AlertDialog aboutDialog;
    private ProgressDialog progressDialog;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean night = false;

    private List<String> funcList = new ArrayList<>(), problemList = new ArrayList<>();
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
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view) {
        llNight = (LinearLayout) view.findViewById(R.id.ll_night);
        llNight.setOnClickListener(vOnClickListener);
        llTheme = (LinearLayout) view.findViewById(R.id.ll_theme);
        llTheme.setOnClickListener(vOnClickListener);
        llFunc = (LinearLayout) view.findViewById(R.id.ll_func);
        llFunc.setOnClickListener(vOnClickListener);
        llProblem = (LinearLayout) view.findViewById(R.id.ll_problem);
        llProblem.setOnClickListener(vOnClickListener);
        llFeedback = (LinearLayout) view.findViewById(R.id.ll_feedback);
        llFeedback.setOnClickListener(vOnClickListener);
        llDownload = (LinearLayout) view.findViewById(R.id.ll_download);
        llDownload.setOnClickListener(vOnClickListener);
        llAboutUs = (LinearLayout) view.findViewById(R.id.ll_about_us);
        llAboutUs.setOnClickListener(vOnClickListener);
        scNight = (SwitchCompat) view.findViewById(R.id.sc_night);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载,请稍后...");
        progressDialog.setCancelable(false);
        showAboutDialog();
    }

    private void initData() {
        app = (mApp) getActivity().getApplication();
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.spf_user), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        night = sharedPreferences.getBoolean(mParams.ISNIGHT, false);
        scNight.setChecked(night);
        scNight.setClickable(false);
        funcList.add("— 工具 —");
        funcList.add("电话、通讯录、信息 : \n" +
                "\t\t\t\t电话 : 您手机上的通话记录\n" +
                "\t\t\t\t通讯录 : 您手机上的联系人\n" +
                "\t\t\t\t信息 : 您手机上的短信记录\n");
        funcList.add("图片、音乐、视频、文档、应用、存储 : \n" +
                "\t\t\t\t您手机上的各种类型的文件, 支持打开、删除、赋值、剪切");
        funcList.add("日历 : \n" +
                "\t\t\t\t正在卖力开发中...");
        funcList.add("时钟 : \n" +
                "\t\t\t\t正在卖力开发中...");
        funcList.add("天气 : \n" +
                "\t\t\t\t天气信息 : 提供全国各城市的天气信息\n" +
                "\t\t\t\t城市选择 : 通过列表点选或搜索名称的方式选择城市\n" +
                "\t\t\t\t — 数据来源 : 中央天气\n\t\t\t\t（http://tj.nineton.cn/Heart/index）");
        funcList.add("备忘录 : \n" +
                "\t\t\t\t正在卖力开发中...");
        funcList.add("计算器 : \n" +
                "\t\t\t\t这是一个支持多项式运算的计算器, 由栈实现");
        funcList.add("翻译 : \n" +
                "\t\t\t\t — 翻译支持 : 必应词典\n\t\t\t\t（http://cn.bing.com/dict/）");
        funcList.add("— 资讯 —");
        funcList.add("文章 : \n" +
                "\t\t\t\t每日一文 : 每天一篇精选优质短篇\n" +
                "\t\t\t\t随机文章 : 点击刷新按钮随机读文章\n" +
                "\t\t\t\t — 数据来源 : " +
                "\n\t\t\t\t 每日一文\n\t\t\t\t（https://meiriyiwen.com）" +
                "\n\t\t\t\t 背景图片\n\t\t\t\t（https://bing.ioliu.cn）");
        funcList.add("趣闻 : \n" +
                "\t\t\t\t每日 : 知乎日报的每日信息\n" +
                "\t\t\t\t热门 : 知乎日报的热门信息\n" +
                "\t\t\t\t分类 : 包括动漫、游戏、财经、电影、音乐、互联网安全等日报\n" +
                "\t\t\t\t — 数据来源 : 知乎日报\n\t\t\t\t（http://news-at.zhihu.com/api）");
        funcList.add("段子 : \n" +
                "\t\t\t\t段子 : 内涵段子的热辣段子\n" +
                "\t\t\t\t图片 : 内涵段子的爆笑图片\n" +
                "\t\t\t\t — 数据来源 : 内涵段子\n\t\t\t\t（http://neihanshequ.com）");
        funcList.add("读书 : \n" +
                "\t\t\t\t书评 : 豆瓣读书的最受欢迎书评\n" +
                "\t\t\t\t书榜 : 爬取呈现豆瓣图书TOP250\n" +
                "\t\t\t\t搜索 : 点击右上角的搜索按钮搜索你想了解的图书信息\n" +
                "\t\t\t\t — 数据来源 : 豆瓣读书\n\t\t\t\t（https://book.douban.com）");
        funcList.add("听音 : \n" +
                "\t\t\t\t乐评 : 豆瓣音乐的最受欢迎乐评\n" +
                "\t\t\t\t乐榜 : 爬取呈现豆瓣音乐TOP250\n" +
                "\t\t\t\t搜索 : 点击右上角的搜索按钮搜索你想了解的音乐信息\n" +
                "\t\t\t\t — 数据来源 : 豆瓣音乐\n\t\t\t\t（https://music.douban.com）");
        funcList.add("观影 : \n" +
                "\t\t\t\t影评 : 豆瓣电影的最受欢迎影评\n" +
                "\t\t\t\t影榜 : 爬取呈现豆瓣电影TOP250\n" +
                "\t\t\t\t搜索 : 点击右上角的搜索按钮搜索你想了解的电影信息\n" +
                "\t\t\t\t — 数据来源 : 豆瓣电影\n\t\t\t\t（https://movie.douban.com）");
        funcList.add("图片 : \n" +
                "\t\t\t\t妹子 : 好看的妹子图\n" +
                "\t\t\t\t美景 : 好看的风景照\n" +
                "\t\t\t\t — 数据来源 : " +
                "\n\t\t\t\t 佳人 : Gank.io\n\t\t\t\t（http://gank.io）" +
                "\n\t\t\t\t 美图 : Pexels Popular Photos\n\t\t\t\t（https://www.pexels.com）");
        funcList.add("漫画 : \n" +
                "\t\t\t\t推荐 : 推荐好看的漫画\n" +
                "\t\t\t\t排行 : 漫画排行榜\n" +
                "\t\t\t\t分类 : 来自有妖气各分区排行榜\n" +
                "\t\t\t\t搜索 : 点击右上角的搜索按钮搜索你想看的漫画\n" +
                "\t\t\t\t — 数据来源 : 有妖气\n\t\t\t\t（https://www.u17.com）");
        funcList.add("视频 : \n" +
                "\t\t\t\t分类 : 来自B站各分区排行榜前十\n" +
                "\t\t\t\t — 数据来源 : BiliBili\n\t\t\t\t（http://api.bilibili.com）");
        funcList.add("游戏 : \n" +
                "\t\t\t\t精选 : 精选各大平台热门游戏\n" +
                "\t\t\t\t优惠、热销、新品、即将推出\n" +
                "\t\t\t\t — 数据来源 : Steam\n\t\t\t\t（https://store.steampowered.com）");
        funcList.add("编程 : \n" +
                "\t\t\t\t个人 : GitHub上Star最多的个人\n" +
                "\t\t\t\t组织 : GitHub上Star最多的组织\n" +
                "\t\t\t\t项目 : GitHub上Star最多的项目\n" +
                "\t\t\t\t趋势 : GitHub上今日最热的项目\n" +
                "\t\t\t\t搜索 : 点击右上角的搜索按钮搜索你想浏览的项目\n" +
                "\t\t\t\t — 数据来源 : GithubRanking\n\t\t\t\t（https://github-ranking.com）");
        problemList.add("Q : 为什么 \"信息\" 列表不显示联系人的名字?\n" +
                "A : 因为1anc3r太懒了, 没有将 \"信息\" 列表和 \"联系人\" 列表进行比对╮(╯_╰)╭. ");
        problemList.add("Q : \"文档\" 和 \"应用\" 加载时间很久?\n" +
                "A : Android没有提供 \"文档\" 的访问接口, 1anc3r是通过遍历文件系统来识别文档的(๑•́ ₃ •̀๑); " +
                " \"应用\" 加载时间长是因为您的手机安装的应用太多了, 第二次加载会快一丢丢(ง •̀_•́)ง. ");
        problemList.add("Q : \"天气\" 中选择的城市没有数据怎么办, 而且别的天气应用都可以定位城市, 为什么你的 \"天气\" 没办法定位城市?\n" +
                "A : \"天气\" 数据来自中央天气, 没有数据的话1anc3r也造不出来. 没有加入定位的原因是中央天气的城市代码和高德地图的城市代码不一样. ");
        problemList.add("Q : \"资讯\" 中数据加载时间长或者加载不出来怎么办?\n" +
                "A : 首先, 检查您的网络是否可用; 其次, 下拉刷新可以解决一部分问题.\n" +
                "(Ps. \"游戏\" 数据来自Steam, \"编程\" 数据来自Github, 这俩货访问速度肯定比其他国内站点慢)");
        problemList.add("Q : 以后还有什么新功能模块?\n" +
                "A : 目前1anc3r的想法是加入日历、时钟、备忘录. 如果你有什么有趣的API的话不妨联系1anc3r.");
        problemList.add("Q : 1anc3r, 1anc3r, 你的程序又崩溃了!\n" +
                "A : 方法一: 通过 \"意见反馈\" 撩一撩1anc3r; 方法二: 发送邮件至huangfangzhi0@foxmail.com");
    }

    private View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == llNight) {
                if (!night) {
//                    editor.putBoolean(mParams.ISNIGHT, true);
//                    editor.apply();
//                    scNight.setChecked(true);
//                    getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    recreate();
                    scNight.setChecked(true);
                    editor.putBoolean(mParams.ISNIGHT, true);
                    editor.apply();
                    Colorful.config(getActivity())
                            .translucent(false)
                            .dark(true)
                            .apply();
                    getActivity().recreate();
                } else {
//                    editor.putBoolean(mParams.ISNIGHT, false);
//                    editor.apply();
//                    scNight.setChecked(false);
//                    getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    recreate();
                    scNight.setChecked(false);
                    editor.putBoolean(mParams.ISNIGHT, false);
                    editor.apply();
                    Colorful.config(getActivity())
                            .translucent(false)
                            .dark(false)
                            .apply();
                    getActivity().recreate();
                }
                night = !night;
            } else if (v == llTheme) {
                ColorPickerDialog dialog = new ColorPickerDialog(getActivity());
                dialog.setTitle("切换主题");
                dialog.setOnColorSelectedListener(new ColorPickerDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(Colorful.ThemeColor themeColor) {
                        Colorful.config(getActivity())
                                .primaryColor(themeColor)
                                .accentColor(themeColor)
                                .translucent(false)
                                .dark(night)
                                .apply();
                        getActivity().recreate();
                    }
                });
                dialog.show();
            } else if (v == llFunc) {
                showListDialog(1, funcList);
            } else if (v == llProblem) {
                showListDialog(2, problemList);
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

    public void showListDialog(int type, List<String> list) {
        View listDialogView = View.inflate(getActivity(), R.layout.dialog_list, null);
        TextView tvType = (TextView) listDialogView.findViewById(R.id.tv_type);
        switch (type) {
            case 1:
                tvType.setText("功能介绍");
                break;
            case 2:
                tvType.setText("常见问题");
                break;
        }
        RecyclerView rvList = (RecyclerView) listDialogView.findViewById(R.id.rv_list);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.Adapter adapter = new SettingAdapter(getActivity(), list);
        rvList.setAdapter(adapter);

        listDialog = new BottomSheetDialog(getActivity());
        listDialog.setContentView(listDialogView);
        listDialog.show();
    }

    private void showAboutDialog() {
        View aboutDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_about, null);
        TextView tvOrganization = (TextView) aboutDialogView.findViewById(R.id.tv_organization);
        tvOrganization.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://www.xiyoumobile.com/");
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
                Uri content_url = Uri.parse("http://www.1anc3r.me");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(aboutDialogView);
        aboutDialog = builder.create();
    }

    private void showRepositoryDialog(List<RepositoryBean> list) {
        View listDialogView = View.inflate(getActivity(), R.layout.dialog_list, null);
        TextView tvType = (TextView) listDialogView.findViewById(R.id.tv_type);
        tvType.setText("我的作品");
        RecyclerView rvList = (RecyclerView) listDialogView.findViewById(R.id.rv_list);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.Adapter adapter = new RepositoryAdapter(getActivity(), list);
        rvList.setAdapter(adapter);
        listDialog = new BottomSheetDialog(getActivity());
        listDialog.setContentView(listDialogView);
        listDialog.show();
    }
}
