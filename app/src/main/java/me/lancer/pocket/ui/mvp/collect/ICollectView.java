package me.lancer.pocket.ui.mvp.collect;

import java.util.List;

import me.lancer.pocket.ui.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface ICollectView extends IBaseView {

    void showResult(int result);

    void showResult(long result);

    void showList(List<CollectBean> list);
}
