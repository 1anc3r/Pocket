package me.lancer.pocket.info.mvp.photo;

import java.util.List;

import me.lancer.pocket.ui.mvp.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class PhotoPresenter implements IBasePresenter<IPhotoView>, IPhotoPresenter {

    private IPhotoView view;
    private PhotoModel model;

    public PhotoPresenter(IPhotoView view) {
        attachView(view);
        model = new PhotoModel(this);
    }

    @Override
    public void attachView(IPhotoView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void download(String url, String title) {
        if (view != null) {
            view.showLoad();
            model.download(url, title);
        }
    }

    @Override
    public void downloadSuccess(String log) {
        if (view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    @Override
    public void downloadFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadPexels(int pager) {
        if (view != null) {
            view.showLoad();
            model.loadPexels(pager);
        }
    }

    @Override
    public void loadPexelsSuccess(List<PhotoBean> list) {
        if (view != null) {
            view.showPexels(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadPexelsFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadGank(int pager) {
        if (view != null) {
            view.showLoad();
            model.loadGank(pager);
        }
    }

    @Override
    public void loadGankSuccess(List<PhotoBean> list) {
        if (view != null) {
            view.showGank(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadGankFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadHuaban(int type, String max) {
        if (view != null) {
            view.showLoad();
            model.loadHuaban(type, max);
        }
    }

    @Override
    public void loadHuabanSuccess(List<PhotoBean> list) {
        if (view != null) {
            view.showHuaban(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadHuabanFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }
}
