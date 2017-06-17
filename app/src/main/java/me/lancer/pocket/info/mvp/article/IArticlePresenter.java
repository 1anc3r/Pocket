package me.lancer.pocket.info.mvp.article;

/**
 * Created by HuangFangzhi on 2017/6/15.
 */

public interface IArticlePresenter {

    void loadDailySuccess(ArticleBean bean);

    void loadDailyFailure(String log);

    void loadRandomSuccess(ArticleBean bean);

    void loadRandomFailure(String log);
}
