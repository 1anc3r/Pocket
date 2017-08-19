package me.lancer.pocket.info.mvp.news;

import java.util.List;

import me.lancer.pocket.ui.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface INewsView extends IBaseView {

    void showLatest(List<NewsBean> list);

    void showBefore(List<NewsBean> list);

    void showHotest(List<NewsBean> list);

    void showPublic(List<NewsBean> list);

    void showList(List<NewsBean> list);

    void showItem(List<NewsBean> list);

    void showDetail(NewsBean bean);
}
