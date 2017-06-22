package me.lancer.pocket.tool.mvp.calendar;

import java.util.List;

import me.lancer.pocket.tool.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface ICalendarView extends IBaseView {

    void showResult(int result);

    void showResult(long result);

    void showList(List<CalendarBean> list);
}
