package me.lancer.pocket.info.mvp.app;

import java.util.List;

import me.lancer.pocket.info.mvp.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class AppPresenter implements IBasePresenter<IAppView>, IAppPresenter {

    private IAppView view;
    private AppModel model;

    public AppPresenter(IAppView view) {
        attachView(view);
        model = new AppModel(this);
    }

    @Override
    public void attachView(IAppView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void loadHomepage(int pager) {
        if (view != null) {
            view.showLoad();
            model.loadHomepage(pager);
        }
    }

    @Override
    public void loadHomepageSuccess(List<AppBean> list) {
        if (view != null) {
            view.showHomepage(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadHomepageFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadSearch(String query, int pager) {
        if (view != null) {
            view.showLoad();
            model.loadSearch(query, pager);
        }
    }

    @Override
    public void loadSearchSuccess(List<AppBean> list) {
        if (view != null) {
            view.showSearch(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadSearchFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadDetail(String id) {
        if (view != null) {
            view.showLoad();
            model.loadDetail(id);
        }
    }

    @Override
    public void loadDetailSuccess(AppBean bean) {
        if (view != null) {
            view.showDetail(bean);
            view.hideLoad();
        }
    }

    @Override
    public void loadDetailFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    @Override
    public void loadDownloadSuccess(String log) {

    }

    @Override
    public void loadDownloadFailure(String log) {

    }

    @Override
    public void loadUpgradeSuccess(List<AppBean> list) {

    }

    @Override
    public void loadUpgradeFailure(String log) {

    }
}
