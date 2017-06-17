package me.lancer.pocket.tool.mvp.translation;

import java.util.List;

import me.lancer.pocket.tool.mvp.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2017/6/16.
 */

public class TranslationPresenter implements IBasePresenter<ITranslationView>, ITranslationPresenter {

    private ITranslationView view;
    private TranslationModel model;

    public TranslationPresenter(ITranslationView view) {
        attachView(view);
        model = new TranslationModel(this);
    }

    @Override
    public void attachView(ITranslationView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void loadTranslation(String keyword) {
        if (view != null) {
            view.showLoad();
            model.loadTranslation(keyword);
        }
    }

    @Override
    public void loadTranslationSuccess(List<TranslationBean> list) {
        if (view != null) {
            view.showTranslation(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadTranslationFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }
}
