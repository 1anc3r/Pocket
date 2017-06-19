package me.lancer.pocket.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.article.activity.ArticleActivity;
import me.lancer.pocket.tool.mvp.app.activity.AppActivity;
import me.lancer.pocket.tool.mvp.calculator.activity.CalculatorActivity;
import me.lancer.pocket.tool.mvp.document.activity.DocumentActivity;
import me.lancer.pocket.tool.mvp.file.activity.FileActivity;
import me.lancer.pocket.tool.mvp.image.activity.ImageActivity;
import me.lancer.pocket.tool.mvp.morse.activity.MorseActivity;
import me.lancer.pocket.tool.mvp.music.activity.MusicActivity;
import me.lancer.pocket.tool.mvp.qrcode.activity.QRCodeActivity;
import me.lancer.pocket.tool.mvp.translation.activity.TranslationActivity;
import me.lancer.pocket.tool.mvp.video.activity.VideoActivity;
import me.lancer.pocket.tool.mvp.weather.activity.WeatherActivity;
import me.lancer.pocket.ui.activity.BlankActivity;
import me.lancer.pocket.ui.adapter.ModelAdapter;
import me.lancer.pocket.ui.bean.ModelBean;

public class BlankFragment extends Fragment implements ModelAdapter.MyItemClickListener, ModelAdapter.MyItemLongClickListener {

    private int index = 0;
    private String[] strTools = {
            "电话", "通讯录", "信息",
            "图片", "音乐", "视频",
            "文档", "应用", "存储",
            /*"日历", "时钟","备忘录",*/
            "天气", "翻译", "摩斯电码",
            "计算器", "二维码", /*"带壳截图"*/};
    private String[] strInfos = {
            "文章", "趣闻", "段子",
            "图书", "音乐", "电影",
            "图片", "漫画", "视频",
            "游戏", "编程",};
    private int[] imgTools = {
            R.mipmap.ic_phone_black_48dp, R.mipmap.ic_people_black_48dp, R.mipmap.ic_message_black_48dp,
            R.mipmap.ic_photo_black_48dp, R.mipmap.ic_music_note_black_48dp, R.mipmap.ic_movie_creation_black_48dp,
            R.mipmap.ic_folder_open_black_48dp, R.mipmap.ic_widgets_black_48dp, R.mipmap.ic_save_black_48dp,
            /*R.mipmap.ic_event_black_48dp, R.mipmap.ic_watch_later_black_48dp,R.mipmap.ic_menu_black_48dp,*/
            R.mipmap.ic_cloud_queue_black_48dp, R.mipmap.ic_translate_black_48dp, R.mipmap.ic_all_inclusive_black_48dp,
            R.mipmap.ic_exposure_plus_1_black_48dp, R.mipmap.ic_crop_free_black_48dp};
    private int[] imgInfos = {
            R.mipmap.ic_insert_drive_file_black_48dp, R.mipmap.ic_lightbulb_outline_black_48dp, R.mipmap.ic_golf_course_black_48dp,
            R.mipmap.ic_book_black_48dp, R.mipmap.ic_music_note_black_48dp, R.mipmap.ic_movie_creation_black_48dp,
            R.mipmap.ic_photo_black_48dp, R.mipmap.ic_mood_black_48dp, R.mipmap.ic_live_tv_black_48dp,
            R.mipmap.ic_extension_black_48dp, R.mipmap.ic_code_black_48dp};

    private RecyclerView mRecyclerView;
    private ModelAdapter mAdapter;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
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
            initView(view, strTools, imgTools);
        } else if (index == 1) {
            initView(view, strInfos, imgInfos);
        } else {

        }
    }

    private void initView(View view, String[] names, int[] icons) {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        for (int i = 0; i < names.length; i++) {
            mList.add(new ModelBean(names[i], icons[i]));
        }
        mAdapter = new ModelAdapter(getActivity(), mList, width);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(View view, int postion) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (index == 0) {
            switch (postion) {
                case 0:
                    intent.putExtra("index", postion - 3);
                    intent.setClass(getActivity(), BlankActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    intent.putExtra("index", postion - 3);
                    intent.setClass(getActivity(), BlankActivity.class);
                    startActivity(intent);
                    break;
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
                    intent.setClass(getActivity(), AppActivity.class);
                    startActivity(intent);
                    break;
                case 8:
                    bundle.putString("method", "in");
                    bundle.putStringArrayList("source", new ArrayList<String>());
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), FileActivity.class);
                    startActivity(intent);
                    break;
                /*case 9:
                    intent.setClass(getActivity(), CalculatorActivity.class);
                    startActivity(intent);
                    break;
                case 10:
                    intent.setClass(getActivity(), CalculatorActivity.class);
                    startActivity(intent);
                    break;*/
                case 9:
                    intent.setClass(getActivity(), WeatherActivity.class);
                    startActivity(intent);
                    break;
                case 10:
                    intent.setClass(getActivity(), TranslationActivity.class);
                    startActivity(intent);
                    break;
                case 11:
                    intent.setClass(getActivity(), MorseActivity.class);
                    startActivity(intent);
                    break;
                case 12:
                    intent.setClass(getActivity(), CalculatorActivity.class);
                    startActivity(intent);
                    break;
                case 13:
                    intent.setClass(getActivity(), QRCodeActivity.class);
                    startActivity(intent);
                    break;
            }
        } else if (index == 1) {
            switch (postion) {
                case 0:
                    intent.setClass(getActivity(), ArticleActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    intent.putExtra("index", postion);
                    intent.setClass(getActivity(), BlankActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    intent.putExtra("index", postion);
                    intent.setClass(getActivity(), BlankActivity.class);
                    startActivity(intent);
                    break;
                case 3:
                    intent.putExtra("index", postion);
                    intent.setClass(getActivity(), BlankActivity.class);
                    startActivity(intent);
                    break;
                case 4:
                    intent.putExtra("index", postion);
                    intent.setClass(getActivity(), BlankActivity.class);
                    startActivity(intent);
                    break;
                case 5:
                    intent.putExtra("index", postion);
                    intent.setClass(getActivity(), BlankActivity.class);
                    startActivity(intent);
                    break;
                case 6:
                    intent.putExtra("index", postion);
                    intent.setClass(getActivity(), BlankActivity.class);
                    startActivity(intent);
                    break;
                case 7:
                    intent.putExtra("index", postion);
                    intent.setClass(getActivity(), BlankActivity.class);
                    startActivity(intent);
                    break;
                case 8:
                    intent.putExtra("index", postion);
                    intent.setClass(getActivity(), BlankActivity.class);
                    startActivity(intent);
                    break;
                case 9:
                    intent.putExtra("index", postion);
                    intent.setClass(getActivity(), BlankActivity.class);
                    startActivity(intent);
                    break;
                case 10:
                    intent.putExtra("index", postion);
                    intent.setClass(getActivity(), BlankActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }
}
