package me.lancer.pocket.info.mvp.news;

import java.util.List;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface INewsPresenter {

    void loadLatestSuccess(List<NewsBean> list);

    void loadLatestFailure(String log);

    void loadBeforeSuccess(List<NewsBean> list);

    void loadBeforeFailure(String log);

    void loadHotestSuccess(List<NewsBean> list);

    void loadHotestFailure(String log);

    void loadPublicSuccess(List<NewsBean> list);

    void loadPublicFailure(String log);

    void loadListSuccess(List<NewsBean> list);

    void loadListFailure(String log);

    void loadItemSuccess(List<NewsBean> list);

    void loadItemFailure(String log);

    void loadDetailSuccess(NewsBean bean);

    void loadDetailFailure(String log);
}