package me.lancer.pocket.info.mvp.article;

import me.lancer.pocket.ui.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/6/15.
 */

public interface IArticleView extends IBaseView {

    void showDaily(ArticleBean bean);

    void showRandom(ArticleBean bean);
}
