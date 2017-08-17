package me.lancer.pocket.ui.mvp.collect;

import java.util.List;

import me.lancer.pocket.ui.mvp.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class CollectPresenter implements IBasePresenter<ICollectView>, ICollectPresenter {

    private ICollectView view;
    private CollectModel model;

    public CollectPresenter(ICollectView view) {
        attachView(view);
        model = new CollectModel(this);
    }

    @Override
    public void attachView(ICollectView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void add(CollectBean bean) {
        if (view != null) {
            view.showLoad();
            model.add(bean);
        }
    }

    public void query() {
        if (view != null) {
            view.showLoad();
            model.query();
        }
    }

    @Override
    public void addCollectResult(long result) {
        if (view != null) {
            view.showResult(result);
            view.hideLoad();
        }
    }

    @Override
    public void queryCollectResult(List<CollectBean> list) {
        if (view != null) {
            view.showList(list);
            view.hideLoad();
        }
    }

    public void modify(CollectBean bean) {
        if (view != null) {
            view.showLoad();
            model.modify(bean);
        }
    }

    @Override
    public void modifyCollectResult(int result) {
        if (view != null) {
            view.showResult(result);
            view.hideLoad();
        }
    }

    public void delete(CollectBean bean) {
        if (view != null) {
            view.showLoad();
            model.delete(bean);
        }
    }

    @Override
    public void deleteCollectResult(int result) {
        if (view != null) {
            view.showResult(result);
            view.hideLoad();
        }
    }
}
