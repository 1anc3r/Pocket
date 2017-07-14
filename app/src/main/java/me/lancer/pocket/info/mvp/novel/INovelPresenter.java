package me.lancer.pocket.info.mvp.novel;

import java.util.List;

/**
 * Created by HuangFangzhi on 2017/5/25.
 */

public interface INovelPresenter {

    void loadRankSuccess(List<NovelBean> list);

    void loadRankFailure(String log);

    void loadCateSuccess(List<NovelBean> list);

    void loadCateFailure(String log);

    void loadSearchSuccess(List<NovelBean> list);

    void loadSearchFailure(String log);

    void loadDetailSuccess(NovelBean bean);

    void loadDetailFailure(String log);

    void switchSourceSuccess(String id);

    void switchSourceFailure(String log);

    void loadChapterSuccess(List<NovelBean.Chapters> list);

    void loadChapterFailure(String log);

    void loadContentSuccess(String content);

    void loadContentFailure(String log);
}
