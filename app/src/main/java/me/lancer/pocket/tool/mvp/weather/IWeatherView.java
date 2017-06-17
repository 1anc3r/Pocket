package me.lancer.pocket.tool.mvp.weather;

import java.util.List;

import me.lancer.pocket.tool.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/6/16.
 */

public interface IWeatherView extends IBaseView {

    void showWeather(WeatherBean bean);

    void showCity(List<CityBean> list);
}
