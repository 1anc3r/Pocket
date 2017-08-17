package me.lancer.pocket.ui.mvp.collect;

import java.util.List;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface ICollectPresenter {

    void addCollectResult(long result);

    void queryCollectResult(List<CollectBean> list);

    void modifyCollectResult(int result);

    void deleteCollectResult(int result);
}