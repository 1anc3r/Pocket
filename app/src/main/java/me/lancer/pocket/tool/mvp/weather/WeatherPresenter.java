package me.lancer.pocket.tool.mvp.weather;

import java.util.List;

import me.lancer.pocket.tool.mvp.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2017/6/16.
 */

public class WeatherPresenter implements IBasePresenter<IWeatherView>, IWeatherPresenter {

    private IWeatherView view;
    private WeatherModel model;

    public WeatherPresenter(IWeatherView view) {
        attachView(view);
        model = new WeatherModel(this);
    }

    @Override
    public void attachView(IWeatherView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void loadWeather(String city) {
        if (view != null) {
            view.showLoad();
            model.loadWeather(city);
        }
    }

    @Override
    public void loadWeatherSuccess(WeatherBean bean) {
        if (view != null) {
            view.showWeather(bean);
            view.hideLoad();
        }
    }

    @Override
    public void loadWeatherFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void loadCity(String content) {
        if (view != null) {
            view.showLoad();
            model.loadCity(content);
        }
    }

    @Override
    public void loadCitySuccess(List<CityBean> list) {
        if (view != null) {
            view.showCity(list);
            view.hideLoad();
        }
    }

    @Override
    public void loadCityFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }
}
