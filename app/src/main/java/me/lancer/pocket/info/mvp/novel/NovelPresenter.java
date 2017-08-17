package me.lancer.pocket.info.mvp.novel;

import java.util.List;

import me.lancer.pocket.ui.mvp.base.IBasePresenter;

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

    public void loadRank() {
        if (view != null) {
            view.showLoad();
            model.loadRankList();
        }
    }

    public void loadRank(String id) {
        if (view != null) {
            view.showLoad();
            model.loadRankItem(id);
        }
    }


    @Override
    public void loadRankSuccess(List<NovelBean> list) {
        if (view != null) {
            view.showRank(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadRankFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadCate() {
        if (view != null) {
            view.showLoad();
            model.loadCateList();
        }
    }

    public void loadCate(String gender, String major, int start, int limit) {
        if (view != null) {
            view.showLoad();
            model.loadCateItem(gender, major, start, limit);
        }
    }

    @Override
    public void loadCateSuccess(List<NovelBean> list) {
        if (view != null) {
            view.showCate(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadCateFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadSearch(String query) {
        if (view != null) {
            view.showLoad();
            model.loadSearch(query);
        }
    }

    @Override
    public void loadSearchSuccess(List<NovelBean> list) {
        if (view != null) {
            view.showSearch(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadSearchFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadDetail(String id) {
        if (view != null) {
            view.showLoad();
            model.loadDetail(id);
        }
    }

    @Override
    public void loadDetailSuccess(NovelBean bean) {
        if (view != null) {
            view.showDetail(bean);
            switchSource(bean.getId());
        }
    }

    @Override
    public void loadDetailFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void switchSource(String id) {
        if (view != null) {
            view.showLoad();
            model.switchSource(id);
        }
    }

    @Override
    public void switchSourceSuccess(String id) {
        if (view != null) {
            loadChapter(id);
        }
    }

    @Override
    public void switchSourceFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadChapter(String id) {
        if (view != null) {
            view.showLoad();
            model.loadChapterList(id);
        }
    }

    @Override
    public void loadChapterSuccess(List<NovelBean.Chapters> list) {
        if (view != null) {
            view.showChapter(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadChapterFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadContent(String link) {
        if (view != null) {
            view.showLoad();
            model.loadContent(link);
        }
    }

    @Override
    public void loadContentSuccess(String content) {
        if (view != null) {
            view.showContent(content);
            view.hideLoad();
        }
    }

    @Override
    public void loadContentFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }
}
