package me.lancer.pocket.tool.mvp.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.util.ContentGetterSetter;
import me.lancer.pocket.util.JsonReader;

/**
 * Created by HuangFangzhi on 2017/6/16.
 */

public class WeatherModel {

    IWeatherPresenter presenter;

    ContentGetterSetter contentGetterSetter = new ContentGetterSetter();
    String headUrl = "http://tj.nineton.cn/Heart/index/all?city=";
    String tailUrl = "&language=zh-chs&unit=c&aqi=city&alarm=1&key=78928e706123c1a8f1766f062bc8676b";
    String cityUrl = "citycode.json";

    public WeatherModel(IWeatherPresenter presenter) {
        this.presenter = presenter;
    }

    public void loadWeather(String city) {
        String content = contentGetterSetter.getContentFromHtml("Weather.loadWeather", headUrl + city + tailUrl);
        WeatherBean bean;
        if (!content.contains("获取失败!")) {
            bean = getWeatherFromContent(content);
            presenter.loadWeatherSuccess(bean);
        } else {
            presenter.loadWeatherFailure(content);
        }
    }

    public void loadCity(String content) {
        List<CityBean> list;
        if (!content.contains("获取失败!")) {
            list = getCityFromContent(content);
            presenter.loadCitySuccess(list);
        } else {
            presenter.loadCityFailure(content);
        }
    }

    private WeatherBean getWeatherFromContent(String content) {
        try {
            WeatherBean bean = new WeatherBean();
            JSONObject jsonObj = new JSONObject(content);
            if (jsonObj.getString("status").equals("OK")) {
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                bean.setCityName(weather.getString("city_name"));
                bean.setCityId(weather.getString("city_id"));
                JSONObject now = weather.getJSONObject("now");
                bean.setWeather(now.getString("text"));
                bean.setTemperature(now.getString("temperature"));
                JSONObject airQuality = now.getJSONObject("air_quality").getJSONObject("city");
                bean.setAirQuality(airQuality.getString("quality"));
                bean.setPm25(airQuality.getString("pm25"));
                JSONObject suggestion = weather.getJSONObject("today").getJSONObject("suggestion");
                bean.setSunrise(weather.getJSONObject("today").getString("sunrise"));
                bean.setSunset(weather.getJSONObject("today").getString("sunset"));
                JSONObject dressing = suggestion.getJSONObject("dressing");
                bean.setDressingBrief(dressing.getString("brief"));
                bean.setDressingDetails(dressing.getString("details"));
                JSONObject uv = suggestion.getJSONObject("uv");
                bean.setUvBrief(uv.getString("brief"));
                bean.setUvDetails(uv.getString("details"));
                JSONObject carWashing = suggestion.getJSONObject("car_washing");
                bean.setCarWashingBrief(carWashing.getString("brief"));
                bean.setCarWashingDetails(carWashing.getString("details"));
                JSONObject travel = suggestion.getJSONObject("travel");
                bean.setTravelBrief(travel.getString("brief"));
                bean.setTravelDetails(travel.getString("details"));
                JSONObject flu = suggestion.getJSONObject("flu");
                bean.setFluBrief(flu.getString("brief"));
                bean.setFluDetails(flu.getString("details"));
                JSONObject sport = suggestion.getJSONObject("sport");
                bean.setSportBrief(sport.getString("brief"));
                bean.setSportDetails(sport.getString("details"));
                List<FutureBean> arr = new ArrayList<>();
                JSONArray futures = weather.getJSONArray("future");
                for (int i = 0; i < futures.length(); i++) {
                    FutureBean itm = new FutureBean();
                    JSONObject future = futures.getJSONObject(i);
                    itm.setDate(future.getString("date"));
                    itm.setDay(future.getString("day"));
                    itm.setHigh(future.getString("high"));
                    itm.setLow(future.getString("low"));
                    itm.setWeather(future.getString("text"));
                    itm.setWind(future.getString("wind"));
                    arr.add(itm);
                }
                bean.setFuture(arr);
            }
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<CityBean> getCityFromContent(String content) {
        try {
            List<CityBean> list = new ArrayList<>();
            JSONArray cityArr = new JSONArray(content);
            for (int i = 0; i < cityArr.length(); i++) {
                CityBean bean = new CityBean();
                JSONObject cityItm = cityArr.getJSONObject(i);
                bean.setId(cityItm.getString("townID"));
                bean.setCityName(cityItm.getString("cityName"));
                bean.setTownName(cityItm.getString("townName"));
                list.add(bean);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
