package me.lancer.pocket.tool.mvp.translation;

import java.util.List;

/**
 * Created by HuangFangzhi on 2017/6/16.
 */

public interface ITranslationPresenter {

    void loadTranslationSuccess(List<TranslationBean> list);

    void loadTranslationFailure(String log);
}
