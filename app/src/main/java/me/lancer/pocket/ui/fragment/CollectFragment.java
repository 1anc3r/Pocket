package me.lancer.pocket.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.article.activity.ArticleActivity;
import me.lancer.pocket.info.mvp.book.activity.BookDetailActivity;
import me.lancer.pocket.info.mvp.chapter.activity.ChapterActivity;
import me.lancer.pocket.info.mvp.game.activity.GameDetailActivity;
import me.lancer.pocket.info.mvp.movie.activity.MovieDetailActivity;
import me.lancer.pocket.info.mvp.music.activity.MusicDetailActivity;
import me.lancer.pocket.info.mvp.news.activity.NewsDetailActivity;
import me.lancer.pocket.info.mvp.photo.activity.PhotoGalleryActivity;
import me.lancer.pocket.ui.activity.MainActivity;
import me.lancer.pocket.ui.activity.SettingActivity;
import me.lancer.pocket.ui.mvp.base.fragment.PresenterFragment;
import me.lancer.pocket.ui.mvp.collect.CollectAdapter;
import me.lancer.pocket.ui.mvp.collect.CollectBean;
import me.lancer.pocket.ui.mvp.collect.CollectPresenter;
import me.lancer.pocket.ui.mvp.collect.ICollectView;

public class CollectFragment extends PresenterFragment<CollectPresenter> implements ICollectView, CollectAdapter.MyItemClickListener, CollectAdapter.MyItemLongClickListener {

    private Toolbar toolbar;
    private ImageView ivImg;
    private RecyclerView rvList;
    private CollectAdapter mAdapter;
    private StaggeredGridLayoutManager layoutManager;

    private List<CollectBean> mList = new ArrayList<>();
    private CollectBean temp;

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
                        mList.clear();
                        mList.addAll((List<CollectBean>) msg.obj);
                        Log.e("Collect: ", "" + mList.size());
                        mAdapter.notifyDataSetChanged();
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

    private Runnable add = new Runnable() {
        @Override
        public void run() {
            presenter.add(temp);
        }
    };

    private Runnable modify = new Runnable() {
        @Override
        public void run() {
            presenter.modify(temp);
        }
    };

    private Runnable delete = new Runnable() {
        @Override
        public void run() {
            presenter.delete(temp);
        }
    };

    private Runnable clear = new Runnable() {
        @Override
        public void run() {
            presenter.delete();
        }
    };

    public CollectFragment() {
    }

    public static CollectFragment newInstance(String param1, String param2) {
        CollectFragment fragment = new CollectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collect, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        inflateMenu();
    }

    private void initView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("收藏");
        ((MainActivity) getActivity()).initDrawer(toolbar);
        ivImg = (ImageView) view.findViewById(R.id.iv_cover);
        if ((Math.random() * 16) > 8) {
            Glide.with(this).load("https://raw.githubusercontent.com/1anc3r/Pocket/master/summer.gif").into(ivImg);
        } else {
            Glide.with(this).load("https://raw.githubusercontent.com/1anc3r/Pocket/master/winter.gif").into(ivImg);
        }
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        mAdapter = new CollectAdapter(getActivity(), mList);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        rvList.setAdapter(mAdapter);
        rvList.setFocusable(true);
        rvList.setFocusableInTouchMode(true);
    }

    private void inflateMenu() {
        toolbar.inflateMenu(R.menu.menu_collect_list);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_refresh:
                        new Thread(query).start();
                        break;
                    case R.id.menu_clear:
                        new Thread(clear).start();
                        break;
                    case R.id.menu_setting:
                        Intent intent2 = new Intent();
                        intent2.setClass(getActivity(), SettingActivity.class);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        List<String> list = new ArrayList<>();
        switch (mList.get(position).getCate()) {
            case 0:
                intent.setClass(getActivity(), ArticleActivity.class);
                intent.putExtra("title", mList.get(position).getTitle());
                intent.putExtra("content", mList.get(position).getCover());
                intent.putExtra("author", mList.get(position).getLink());
                startActivity(intent);
                break;
            case 1:
                intent.setClass(getActivity(), NewsDetailActivity.class);
                intent.putExtra("title", mList.get(position).getTitle());
                intent.putExtra("img", mList.get(position).getCover());
                intent.putExtra("link", mList.get(position).getLink());
                startActivity(intent);
                break;
            case 3:
            case 4:
                intent.setClass(getActivity(), BookDetailActivity.class);
                intent.putExtra("type", mList.get(position).getCate() - 3);
                intent.putExtra("title", mList.get(position).getTitle());
                intent.putExtra("img", mList.get(position).getCover());
                intent.putExtra("link", mList.get(position).getLink());
                startActivity(intent);
                break;
            case 5:
            case 6:
                intent.setClass(getActivity(), MusicDetailActivity.class);
                intent.putExtra("type", mList.get(position).getCate() - 5);
                intent.putExtra("title", mList.get(position).getTitle());
                intent.putExtra("img", mList.get(position).getCover());
                intent.putExtra("link", mList.get(position).getLink());
                startActivity(intent);
                break;
            case 7:
            case 8:
                intent.setClass(getActivity(), MovieDetailActivity.class);
                intent.putExtra("type", mList.get(position).getCate() - 7);
                intent.putExtra("title", mList.get(position).getTitle());
                intent.putExtra("img", mList.get(position).getCover());
                intent.putExtra("link", mList.get(position).getLink());
                startActivity(intent);
                break;
            case 10:
                list.add(mList.get(position).getCover());
                intent.putStringArrayListExtra("gallery", (ArrayList<String>) list);
                intent.putExtra("position", 0);
                intent.setClass(getActivity(), PhotoGalleryActivity.class);
                startActivity(intent);
                break;
            case 11:
                intent.setClass(getActivity(), ChapterActivity.class);
                intent.putExtra("title", mList.get(position).getTitle());
                intent.putExtra("cover", mList.get(position).getCover());
                intent.putExtra("link", mList.get(position).getLink());
                startActivity(intent);
                break;
            case 13:
                intent.setClass(getActivity(), GameDetailActivity.class);
                intent.putExtra("title", mList.get(position).getTitle());
                intent.putExtra("img", mList.get(position).getCover());
                intent.putExtra("link", mList.get(position).getLink());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(query).start();
    }

    @Override
    public void onItemLongClick(View view, final int position) {

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
