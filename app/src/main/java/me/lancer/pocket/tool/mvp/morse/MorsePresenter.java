package me.lancer.pocket.tool.mvp.morse;

import java.util.Map;

import me.lancer.pocket.ui.mvp.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2017/6/19.
 */

public class MorsePresenter implements IBasePresenter<IMorseView>, IMorsePresenter {

    private IMorseView view;
    private MorseModel model;

    public MorsePresenter(IMorseView view) {
        attachView(view);
        model = new MorseModel(this);
    }

    @Override
    public void attachView(IMorseView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void loadMorse(String content) {
        if (view != null) {
            view.showLoad();
            model.loadMorse(content);
        }
    }

    @Override
    public void loadMorseSuccess(Map<String, String> map) {
        if (view != null) {
            view.showMorse(map);
            view.hideLoad();
        }
    }

    @Override
    public void loadMorseFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }
}
