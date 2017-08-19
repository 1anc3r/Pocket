package me.lancer.pocket.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.article.activity.ArticleActivity;
import me.lancer.pocket.ui.activity.BlankActivity;
import me.lancer.pocket.ui.mvp.model.ModelAdapter;
import me.lancer.pocket.ui.mvp.model.ModelBean;
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

public class BlankFragment extends Fragment implements ModelAdapter.MyItemClickListener, ModelAdapter.MyItemLongClickListener {

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
            R.mipmap.ic_phone_black_48dp, R.mipmap.ic_people_black_48dp, R.mipmap.ic_message_black_48dp,
            R.mipmap.ic_photo_black_48dp, R.mipmap.ic_music_note_black_48dp, R.mipmap.ic_movie_creation_black_48dp,
            R.mipmap.ic_folder_open_black_48dp, R.mipmap.ic_widgets_black_48dp, R.mipmap.ic_save_black_48dp,
            R.mipmap.ic_event_black_48dp, R.mipmap.ic_assignment_turned_in_48pt_3x, R.mipmap.ic_cloud_queue_black_48dp,
            R.mipmap.ic_translate_black_48dp, R.mipmap.ic_all_inclusive_black_48dp, R.mipmap.ic_calculator_black_48dp, R.mipmap.ic_qrcode_black_48dp};
    private int[] imgInfos = {
            R.mipmap.ic_insert_drive_file_black_48dp, R.mipmap.ic_lightbulb_outline_black_48dp, R.mipmap.ic_golf_course_black_48dp,
            R.mipmap.ic_book_black_48dp, R.mipmap.ic_music_note_black_48dp, R.mipmap.ic_movie_creation_black_48dp,
            R.mipmap.ic_local_library_black_48dp, R.mipmap.ic_photo_black_48dp, R.mipmap.ic_mood_black_48dp,
            R.mipmap.ic_live_tv_black_48dp, R.mipmap.ic_extension_black_48dp, R.mipmap.ic_code_black_48dp};

    private RecyclerView rvList;
    private ModelAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<ModelBean> mList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        index = this.getArguments().getInt(getString(R.string.index));
        if (index == 0) {
            initView(strTools, imgTools);
        } else if (index == 1) {
            initView(strInfos, imgInfos);
        }
    }

    private void initView(String[] names, int[] icons) {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        rvList = (RecyclerView) getView().findViewById(R.id.rv_list);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(mLinearLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        for (int i = 0; i < names.length; i++) {
            mList.add(new ModelBean(names[i], icons[i]));
        }
        mAdapter = new ModelAdapter(getActivity(), mList, width);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        rvList.setAdapter(mAdapter);
        rvList.setFocusable(true);
        rvList.setFocusableInTouchMode(true);
        rvList.setOnKeyListener(OnKeyListener);
    }

    private View.OnKeyListener OnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                mAdapter.notifyDataSetChanged();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onItemClick(View view, int postion) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (index == 0) {
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
        } else if (index == 1) {
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
}
