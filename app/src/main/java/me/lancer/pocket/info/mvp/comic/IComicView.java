package me.lancer.pocket.info.mvp.comic;

import java.util.List;

import me.lancer.pocket.info.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/5/25.
 */

public interface IComicView extends IBaseView {

    void showList(List<ComicBean> list);

    void showRank(List<ComicBean> list);

    void showSort(List<ComicBean> list);
}
