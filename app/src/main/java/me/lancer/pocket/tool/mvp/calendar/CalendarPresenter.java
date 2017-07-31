package me.lancer.pocket.tool.mvp.calendar;

import java.util.List;

import me.lancer.pocket.ui.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class CalendarPresenter implements IBasePresenter<ICalendarView>, ICalendarPresenter {

    private ICalendarView view;
    private CalendarModel model;

    public CalendarPresenter(ICalendarView view) {
        attachView(view);
        model = new CalendarModel(this);
    }

    @Override
    public void attachView(ICalendarView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void add(CalendarBean bean) {
        if (view != null) {
            view.showLoad();
            model.add(bean);
        }
    }

    @Override
    public void addCalendarResult(long result) {
        if (view != null) {
            view.showResult(result);
            view.hideLoad();
        }
    }

    public void query() {
        if (view != null) {
            view.showLoad();
            model.query();
        }
    }

    @Override
    public void queryCalendarResult(List<CalendarBean> list) {
        if (view != null) {
            view.showList(list);
            view.hideLoad();
        }
    }

    public void modify(CalendarBean bean) {
        if (view != null) {
            view.showLoad();
            model.modify(bean);
        }
    }

    @Override
    public void modifyCalendarResult(int result) {
        if (view != null) {
            view.showResult(result);
            view.hideLoad();
        }
    }

    public void delete(CalendarBean bean) {
        if (view != null) {
            view.showLoad();
            model.delete(bean);
        }
    }

    @Override
    public void deleteCalendarResult(int result) {
        if (view != null) {
            view.showResult(result);
            view.hideLoad();
        }
    }
}
