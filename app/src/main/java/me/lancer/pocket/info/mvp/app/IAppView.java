package me.lancer.pocket.info.mvp.app;

import java.util.List;

import me.lancer.pocket.ui.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface IAppView extends IBaseView {

    void showHomepage(List<AppBean> list);

    void showSearch(List<AppBean> list);

    void showDetail(AppBean bean);

    void showDownload(String log);

    void showUpgrade(List<AppBean> list);
}
