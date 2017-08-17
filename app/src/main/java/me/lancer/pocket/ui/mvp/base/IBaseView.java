package me.lancer.pocket.ui.mvp.base;

import java.util.List;

import me.lancer.pocket.ui.mvp.collect.CollectBean;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface IBaseView {

    void showMsg(String log);

    void showLoad();

    void hideLoad();
}
