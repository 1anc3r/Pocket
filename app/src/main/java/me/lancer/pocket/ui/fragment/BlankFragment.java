package me.lancer.pocket.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.article.activity.ArticleActivity;
import me.lancer.pocket.tool.mvp.calculator.activity.CalculatorActivity;
import me.lancer.pocket.tool.mvp.calendar.activity.CalendarActivity;
import me.lancer.pocket.tool.mvp.document.activity.DocumentActivity;
import me.lancer.pocket.tool.mvp.file.activity.FileActivity;
import me.lancer.pocket.tool.mvp.image.activity.ImageActivity;
import me.lancer.pocket.tool.mvp.morse.activity.MorseActivity;
import me.lancer.pocket.tool.mvp.music.activity.MusicActivity;
import me.lancer.pocket.tool.mvp.qrcode.activity.QRCodeActivity;
import me.lancer.pocket.tool.mvp.todo.tasks.TasksActivity;
import me.lancer.pocket.tool.mvp.translation.activity.TranslationActivity;
import me.lancer.pocket.tool.mvp.video.activity.VideoActivity;
import me.lancer.pocket.tool.mvp.weather.activity.WeatherActivity;
import me.lancer.pocket.ui.activity.BlankActivity;
import me.lancer.pocket.ui.application.App;
import me.lancer.pocket.ui.mvp.base.fragment.PresenterFragment;
import me.lancer.pocket.ui.mvp.collect.CollectBean;
import me.lancer.pocket.ui.mvp.collect.CollectPresenter;
import me.lancer.pocket.ui.mvp.collect.CollectUtil;
import me.lancer.pocket.ui.mvp.collect.ICollectView;
import me.lancer.pocket.ui.mvp.model.ModelAdapter;
import me.lancer.pocket.ui.mvp.model.ModelBean;

public class BlankFragment extends PresenterFragment<CollectPresenter> implements ICollectView, ModelAdapter.MyItemClickListener, ModelAdapter.MyItemLongClickListener {

    private int index = 0;
    private String[] strTools = {
            "电话", "通讯录", "信息",
            "图片", "音乐", "视频",
            "文档", "应用", "存储",
            "日历", "To-Do", "天气",
            "翻译", "摩斯电码", "计算器", "二维码",};
    private String[] strInfos = {
            "文章", "趣闻", "段子",
            "图书", "音乐", "电影",
            "小说", "图片", "漫画",
            "视频", "游戏", "编程",};
    private int[] imgTools = {
            R.drawable.ic_phone_black_24dp, R.drawable.ic_people_black_24dp, R.drawable.ic_message_black_24dp,
            R.drawable.ic_photo_black_24dp, R.drawable.ic_audiotrack_black_24dp, R.drawable.ic_movie_black_24dp,
            R.drawable.ic_folder_open_black_24dp, R.drawable.ic_widgets_black_24dp, R.drawable.ic_save_black_24dp,
            R.drawable.ic_event_black_24dp, R.drawable.ic_assignment_turned_in_black_24dp, R.drawable.ic_cloud_queue_black_24dp,
            R.drawable.ic_translate_black_24dp, R.drawable.ic_all_inclusive_black_24dp, R.mipmap.ic_calculator_black_48dp, R.mipmap.ic_qrcode_black_48dp};
    private int[] imgInfos = {
            R.drawable.ic_insert_drive_file_black_24dp, R.drawable.ic_lightbulb_outline_black_24dp, R.drawable.ic_golf_course_black_24dp,
            R.drawable.ic_book_black_24dp, R.drawable.ic_audiotrack_black_24dp, R.drawable.ic_movie_black_24dp,
            R.drawable.ic_local_library_black_24dp, R.drawable.ic_photo_black_24dp, R.drawable.ic_mood_black_24dp,
            R.drawable.ic_live_tv_black_24dp, R.drawable.ic_extension_black_24dp, R.drawable.ic_code_black_24dp};

    private RecyclerView rvList;
    private ModelAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private List<ModelBean> list = new ArrayList<>();
    private List<CollectBean> temp = new ArrayList<>();

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
                    new Thread(query).start();
                    break;
                case 4:
                    if (msg.obj != null) {
                        temp = (List<CollectBean>) msg.obj;
                        if(index == 0) {
                            for (ModelBean mitem : list) {
                                for (CollectBean citem : temp) {
                                    if (mitem.getId() == citem.getModel()) {
                                        mitem.addItem(citem);
                                    }
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    private Runnable query = new Runnable() {
        @Override
        public void run() {
            presenter.query();
        }
    };

    private View.OnKeyListener OnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                adapter.notifyDataSetChanged();
                return true;
            }
            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        index = this.getArguments().getInt(getString(R.string.index));
        if (index == 1) {
            initView(strTools, imgTools);
        } else if (index == 0) {
            initView(strInfos, imgInfos);
        }
    }

    private void initView(String[] names, int[] icons) {
        rvList = (RecyclerView) getView().findViewById(R.id.rv_list);
        layoutManager = new StaggeredGridLayoutManager(((App) getActivity().getApplication()).getColNumber(), StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        for (int i = 0; i < names.length; i++) {
            ModelBean bean = null;
            if (index == 0) {
                bean = new ModelBean(i, names[i], icons[i]);
            } else if (index == 1) {
                bean = new ModelBean(names[i], icons[i]);
            }
            list.add(bean);
        }
        adapter = new ModelAdapter(getActivity(), index,  list);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        rvList.setAdapter(adapter);
        rvList.setFocusable(true);
        rvList.setFocusableInTouchMode(true);
        rvList.setOnKeyListener(OnKeyListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        layoutManager = new StaggeredGridLayoutManager(((App) getActivity().getApplication()).getColNumber(), StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        new Thread(query).start();
    }

    @Override
    public void onItemClick(View view, int postion) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (index == 1) {
            switch (postion) {
                case 0:
                case 1:
                case 2:
                    intent.putExtra("index", postion - 3);
                    intent.setClass(getActivity(), BlankActivity.class);
                    startActivity(intent);
                    break;
                case 3:
                    intent.setClass(getActivity(), ImageActivity.class);
                    startActivity(intent);
                    break;
                case 4:
                    intent.setClass(getActivity(), MusicActivity.class);
                    startActivity(intent);
                    break;
                case 5:
                    intent.setClass(getActivity(), VideoActivity.class);
                    startActivity(intent);
                    break;
                case 6:
                    intent.setClass(getActivity(), DocumentActivity.class);
                    startActivity(intent);
                    break;
                case 7:
                    intent.putExtra("index", 1024);
                    intent.setClass(getActivity(), BlankActivity.class);
                    startActivity(intent);
                    break;
                case 8:
                    bundle.putString("method", "in");
                    bundle.putStringArrayList("source", new ArrayList<String>());
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), FileActivity.class);
                    startActivity(intent);
                    break;
                case 9:
                    intent.setClass(getActivity(), CalendarActivity.class);
                    startActivity(intent);
                    break;
                case 10:
                    intent.setClass(getActivity(), TasksActivity.class);
                    startActivity(intent);
                    break;
                case 11:
                    intent.setClass(getActivity(), WeatherActivity.class);
                    startActivity(intent);
                    break;
                case 12:
                    intent.setClass(getActivity(), TranslationActivity.class);
                    startActivity(intent);
                    break;
                case 13:
                    intent.setClass(getActivity(), MorseActivity.class);
                    startActivity(intent);
                    break;
                case 14:
                    intent.setClass(getActivity(), CalculatorActivity.class);
                    startActivity(intent);
                    break;
                case 15:
                    intent.setClass(getActivity(), QRCodeActivity.class);
                    startActivity(intent);
                    break;
            }
        } else if (index == 0) {
            if (postion == 0) {
                intent.setClass(getActivity(), ArticleActivity.class);
                startActivity(intent);
            } else {
                intent.putExtra("index", postion);
                intent.setClass(getActivity(), BlankActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onItemLongClick(View view, final int postion) {

    }

    @Override
    protected CollectPresenter onCreatePresenter() {
        return new CollectPresenter(this);
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
    public void showResult(int result) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = result;
        handler.sendMessage(msg);
    }

    @Override
    public void showResult(long result) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = result;
        handler.sendMessage(msg);
    }

    @Override
    public void showList(List<CollectBean> list) {
        Message msg = new Message();
        msg.what = 4;
        msg.obj = list;
        handler.sendMessage(msg);
    }
}
