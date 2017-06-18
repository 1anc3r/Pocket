package me.lancer.pocket.info.mvp.knowledge;

import java.util.List;

import me.lancer.pocket.info.mvp.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class KnowledgePresenter implements IBasePresenter<IKnowledgeView>, IKnowledgePresenter {

    private IKnowledgeView view;
    private KnowledgeModel model;

    public KnowledgePresenter(IKnowledgeView view) {
        attachView(view);
        model = new KnowledgeModel(this);
    }

    @Override
    public void attachView(IKnowledgeView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void loadClassify(int type){
        if (view != null) {
            view.showLoad();
            model.classify(type);
        }
    }

    @Override
    public void loadClassifySuccess(List<KnowledgeBean> list) {
        if (view != null) {
            view.showClassify(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadClassifyFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadList(int type, int id){
        if (view != null) {
            view.showLoad();
            model.list(type, id);
        }
    }

    @Override
    public void loadListSuccess(List<KnowledgeBean> list) {
        if (view != null) {
            view.showList(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadListFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadNews(int type, int id){
        if (view != null) {
            view.showLoad();
            model.news(type, id);
        }
    }

    @Override
    public void loadNewsSuccess(List<KnowledgeBean> list) {
        if (view != null) {
            view.showNews(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadNewsFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadShow(int type, int id){
        if (view != null) {
            view.showLoad();
            model.show(type, id);
        }
    }

    @Override
    public void loadShowSuccess(KnowledgeBean bean) {
        if (view != null) {
            view.showShow(bean);
            view.hideLoad();
        }
    }

    @Override
    public void loadShowFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadSearch(int type, String keyword){
        if (view != null) {
            view.showLoad();
            model.search(type, keyword);
        }
    }

    @Override
    public void loadSearchSuccess(List<KnowledgeBean> list) {
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
}
