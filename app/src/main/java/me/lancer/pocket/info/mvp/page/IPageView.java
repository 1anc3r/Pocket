package me.lancer.pocket.info.mvp.page;

import java.util.List;

import me.lancer.pocket.info.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/5/26.
 */

public interface IPageView extends IBaseView {

    void showList(List<PageBean> list);
}