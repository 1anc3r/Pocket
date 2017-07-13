package me.lancer.pocket.info.mvp.novel;

import java.util.List;

import me.lancer.pocket.info.mvp.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2017/5/25.
 */

public class NovelPresenter implements IBasePresenter<INovelView>, INovelPresenter {

    private INovelView view;
    private NovelModel model;

    public NovelPresenter(INovelView view) {
        attachView(view);
        model = new NovelModel(this);
    }

    @Override
    public void attachView(INovelView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }


}
