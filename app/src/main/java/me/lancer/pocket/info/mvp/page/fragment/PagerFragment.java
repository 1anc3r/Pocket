package me.lancer.pocket.info.mvp.page.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.mvp.base.fragment.PresenterFragment;
import me.lancer.pocket.info.mvp.photo.IPhotoView;
import me.lancer.pocket.info.mvp.photo.PhotoBean;
import me.lancer.pocket.info.mvp.photo.PhotoPresenter;
import me.lancer.pocket.ui.application.Params;
import me.lancer.pocket.ui.mvp.collect.CollectBean;
import me.lancer.pocket.ui.mvp.collect.CollectUtil;

public class PagerFragment extends PresenterFragment<PhotoPresenter> implements IPhotoView {

    private String link;
    private ImageView imageView;
    private Button btnFavorite, btnShare, btnDownload;

    private List<CollectBean> temps = new ArrayList<>();
    private CollectBean temp = new CollectBean();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
            }
        }
    };

    private Runnable download = new Runnable() {
        @Override
        public void run() {
            presenter.download(link, (new Date().toString()));
        }
    };

    public static PagerFragment newInstance(String link) {
        Bundle args = new Bundle();
        args.putString("link", link);
        PagerFragment fragment = new PagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        link = getArguments().getString("link");
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        btnFavorite = (Button) view.findViewById(R.id.btn_favorite);
        temps = CollectUtil.query(link, link);
        if(temps.size() == 1) {
            btnFavorite.setBackgroundResource(R.mipmap.ic_favorite_white_24dp);
        } else {
            btnFavorite.setBackgroundResource(R.mipmap.ic_favorite_border_white_24dp);
        }
        btnShare = (Button) view.findViewById(R.id.btn_share);
        btnDownload = (Button) view.findViewById(R.id.btn_download);
        btnFavorite.setOnClickListener(vOnClickListener);
        btnShare.setOnClickListener(vOnClickListener);
        btnDownload.setOnClickListener(vOnClickListener);
    }

    private View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnFavorite) {
                if(temps.size() == 1) {
                    btnFavorite.setBackgroundResource(R.mipmap.ic_favorite_border_white_24dp);
                    CollectUtil.delete(temps.get(0));
                    temps = CollectUtil.query(link, link);
                } else {
                    btnFavorite.setBackgroundResource(R.mipmap.ic_favorite_white_24dp);
                    temp.setType(1);
                    temp.setCate(10);
                    temp.setCover(link);
                    temp.setTitle(link);
                    temp.setLink(link);
                    CollectUtil.add(temp);
                    temps = CollectUtil.query(link, link);
                }
            } else if (view == btnShare) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, "快看看我发现了什么宝贝(ง •̀_•́)ง\n" + link + "\n分享自@" + getResources().getString(R.string.app_name));
                startActivity(Intent.createChooser(intent, "分享到"));
            } else if (view == btnDownload) {
                new Thread(download).start();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        ViewCompat.setTransitionName(imageView, Params.TRANSITION_PIC);
        Glide.with(this).load(link).into(imageView);
    }

    @Override
    protected PhotoPresenter onCreatePresenter() {
        return new PhotoPresenter(this);
    }

    @Override
    public void showMsg(String log) {
        showSnackbar(imageView, log);
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
    public void showLatest(List<PhotoBean> list) {

    }

    @Override
    public void showTheme(List<PhotoBean> list) {

    }

    @Override
    public void showWelfare(List<PhotoBean> list) {

    }
}