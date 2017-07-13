package me.lancer.pocket.info.mvp.novel;

import java.util.List;

import me.lancer.pocket.info.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/5/25.
 */

public interface INovelView extends IBaseView {

    void showRank(List<NovelBean> list);

    void showCate(List<NovelBean> list);

    void showSearch(List<NovelBean> list);

    void showNovel(NovelBean bean);

    void showChapter(List<NovelBean.Chapters> list);
}
