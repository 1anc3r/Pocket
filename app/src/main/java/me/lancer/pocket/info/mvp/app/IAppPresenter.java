package me.lancer.pocket.info.mvp.app;

import java.util.List;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface IAppPresenter {

    void loadHomepageSuccess(List<AppBean> list);

    void loadHomepageFailure(String log);

    void loadSearchSuccess(List<AppBean> list);

    void loadSearchFailure(String log);

    void loadDetailSuccess(AppBean bean);

    void loadDetailFailure(String log);

    void loadDownloadSuccess(String log);

    void loadDownloadFailure(String log);

    void loadUpgradeSuccess(List<AppBean> list);

    void loadUpgradeFailure(String log);
}