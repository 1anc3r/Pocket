package me.lancer.pocket.info.mvp.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.app.AppBean;
import me.lancer.pocket.info.mvp.app.AppPresenter;
import me.lancer.pocket.info.mvp.app.IAppView;
import me.lancer.pocket.info.mvp.app.adapter.AppAdapter;
import me.lancer.pocket.info.mvp.base.activity.PresenterActivity;

public class AppSearchActivity extends PresenterActivity<AppPresenter> implements IAppView {

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
