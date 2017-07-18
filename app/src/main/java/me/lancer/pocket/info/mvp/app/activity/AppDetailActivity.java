package me.lancer.pocket.info.mvp.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.app.AppBean;
import me.lancer.pocket.info.mvp.app.AppPresenter;
import me.lancer.pocket.info.mvp.app.IAppView;
import me.lancer.pocket.info.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.mainui.application.Params;
import me.lancer.pocket.mainui.view.htmltextview.HtmlHttpImageGetter;
import me.lancer.pocket.mainui.view.htmltextview.HtmlTextView;

public class AppDetailActivity extends PresenterActivity<AppPresenter> implements IAppView {

    @Override
    protected AppPresenter onCreatePresenter() {
        return null;
    }

    @Override
    public void showMsg(String log) {

    }

    @Override
    public void showLoad() {

    }

    @Override
    public void hideLoad() {

    }

    @Override
    public void showHomepage(List<AppBean> list) {

    }

    @Override
    public void showSearch(List<AppBean> list) {

    }

    @Override
    public void showDetail(AppBean bean) {

    }

    @Override
    public void showDownload(String log) {

    }

    @Override
    public void showUpgrade(List<AppBean> list) {

    }
}
