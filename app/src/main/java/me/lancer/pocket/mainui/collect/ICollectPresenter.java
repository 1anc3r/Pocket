package me.lancer.pocket.mainui.collect;

import java.util.List;

import me.lancer.pocket.tool.mvp.calendar.CalendarBean;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface ICollectPresenter {

    void addCollectResult(long result);

    void queryCollectResult(List<CollectBean> list);

    void modifyCollectResult(int result);

    void deleteCollectResult(int result);
}