package me.lancer.pocket.info.mvp.chapter;

import java.util.List;

/**
 * Created by HuangFangzhi on 2017/5/25.
 */

public interface IChapterPresenter {

    void loadListSuccess(List<ChapterBean> list);

    void loadListFailure(String log);
}
