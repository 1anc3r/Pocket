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

    void loadNovelSuccess(NovelBean bean);

    void loadNovelFailure(String log);

    void switchSourceSuccess(String id);

    void switchSourceFailure(String log);

    void loadChapterSuccess(List<NovelBean.Chapters> list);

    void loadChapterFailure(String log);
}
