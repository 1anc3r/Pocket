package me.lancer.pocket.ui.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;

import me.lancer.pocket.info.mvp.news.NewsBean;
import me.lancer.pocket.info.mvp.news.activity.NewsDetailActivity;
import me.lancer.pocket.ui.application.Params;

/**
 * Created by HuangFangzhi on 2017/8/22.
 */

public class ImageHolderView implements Holder<String> {

    private ImageView ivCover;

    @Override
    public View createView(Context context) {
        ivCover = new ImageView(context);
        ivCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return ivCover;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        ViewCompat.setTransitionName(ivCover, Params.TRANSITION_PIC);
        Glide.with(context).load(data).into(ivCover);
    }
}