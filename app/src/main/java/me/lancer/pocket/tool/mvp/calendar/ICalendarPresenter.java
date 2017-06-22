package me.lancer.pocket.tool.mvp.calendar;

import java.util.List;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface ICalendarPresenter {

    void addCalendarResult(long result);

    void queryCalendarResult(List<CalendarBean> list);

    void modifyCalendarResult(int result);

    void deleteCalendarResult(int result);
}