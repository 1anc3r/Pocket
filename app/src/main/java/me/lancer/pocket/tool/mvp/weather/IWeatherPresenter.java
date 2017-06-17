package me.lancer.pocket.tool.mvp.weather;

import java.util.List;

/**
 * Created by HuangFangzhi on 2017/6/16.
 */

public interface IWeatherPresenter {

    void loadWeatherSuccess(WeatherBean bean);

    void loadWeatherFailure(String log);

    void loadCitySuccess(List<CityBean> list);

    void loadCityFailure(String log);
}
