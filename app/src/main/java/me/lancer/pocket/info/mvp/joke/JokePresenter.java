package me.lancer.pocket.info.mvp.joke;

import java.util.List;

import me.lancer.pocket.ui.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class JokePresenter implements IBasePresenter<IJokeView>, IJokePresenter {

    private IJokeView view;
    private JokeModel model;

    public JokePresenter(IJokeView view) {
        attachView(view);
        model = new JokeModel(this);
    }

    @Override
    public void attachView(IJokeView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void loadText() {
        if (view != null) {
            view.showLoad();
            model.loadText();
        }
    }

    @Override
    public void loadTextSuccess(List<JokeBean> list) {
        if (view != null) {
            view.showText(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadTextFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadImage() {
        if (view != null) {
            view.showLoad();
            model.loadImage();
        }
    }

    @Override
    public void loadImageSuccess(List<JokeBean> list) {
        if (view != null) {
            view.showImage(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadImageFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadVideo() {
        if (view != null) {
            view.showLoad();
            model.loadVideo();
        }
    }

    @Override
    public void loadVideoSuccess(List<JokeBean> list) {
        if (view != null) {
            view.showVideo(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadVideoFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }
}
