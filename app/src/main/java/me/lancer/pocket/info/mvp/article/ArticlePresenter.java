package me.lancer.pocket.info.mvp.article;

import me.lancer.pocket.ui.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2017/6/15.
 */

public class ArticlePresenter implements IBasePresenter<IArticleView>, IArticlePresenter {

    private IArticleView view;
    private ArticleModel model;

    public ArticlePresenter(IArticleView view) {
        attachView(view);
        model = new ArticleModel(this);
    }

    @Override
    public void attachView(IArticleView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void loadDaily() {
        if (view != null) {
            view.showLoad();
            model.loadDaily();
        }
    }

    @Override
    public void loadDailySuccess(ArticleBean bean) {
        if (view != null) {
            view.showDaily(bean);
            view.hideLoad();
        }
    }

    @Override
    public void loadDailyFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadRandom() {
        if (view != null) {
            view.showLoad();
            model.loadRandom();
        }
    }

    @Override
    public void loadRandomSuccess(ArticleBean bean) {
        if (view != null) {
            view.showRandom(bean);
            view.hideLoad();
        }
    }

    @Override
    public void loadRandomFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }
}
