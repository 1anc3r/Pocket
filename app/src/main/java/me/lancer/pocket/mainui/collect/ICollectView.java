package me.lancer.pocket.mainui.collect;

import java.util.List;

import me.lancer.pocket.tool.mvp.base.IBaseView;
import me.lancer.pocket.tool.mvp.calendar.CalendarBean;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface ICollectView extends IBaseView {

    void showResult(int result);

    void showResult(long result);

    void showList(List<CollectBean> list);
}
