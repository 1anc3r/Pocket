package me.lancer.pocket.tool.mvp.translation;

import java.util.List;

import me.lancer.pocket.ui.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/6/16.
 */

public interface ITranslationView extends IBaseView {

    void showTranslation(List<TranslationBean> list);
}
