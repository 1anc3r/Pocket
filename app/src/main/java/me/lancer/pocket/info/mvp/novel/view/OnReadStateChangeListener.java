package me.lancer.pocket.info.mvp.novel.view;

/**
 * Created by HuangFangzhi on 2017/7/13.
 */

public interface OnReadStateChangeListener {

    void onChapterChanged(int chapter);

    void onPageChanged(int chapter, int page);

    void onLoadChapterFailure(int chapter);

    void onCenterClick();

    void onFlip();
}
