package me.lancer.pocket.tool.mvp.weather;

import java.util.List;

/**
 * Created by HuangFangzhi on 2017/6/16.
 */

public class WeatherBean {

    private String cityName;
    private String cityId;
    private String weather;
    private String temperature;
    private String airQuality;
    private String pm25;
    private String sunrise;
    private String sunset;
    private String dressingBrief;//穿衣
    private String dressingDetails;
    private String uvBrief;//紫外线
    private String uvDetails;
    private String carWashingBrief;//洗车
    private String carWashingDetails;
    private String travelBrief;//旅游
    private String travelDetails;
    private String fluBrief;//流感
    private String fluDetails;
    private String sportBrief;//运动
    private String sportDetails;
    private List<FutureBean> future;

    public WeatherBean() {

    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(String airQuality) {
        this.airQuality = airQuality;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getDressingBrief() {
        return dressingBrief;
    }

    public void setDressingBrief(String dressingBrief) {
        this.dressingBrief = dressingBrief;
    }

    public String getDressingDetails() {
        return dressingDetails;
    }

    public void setDressingDetails(String dressingDetails) {
        this.dressingDetails = dressingDetails;
    }

    public String getUvBrief() {
        return uvBrief;
    }

    public void setUvBrief(String uvBrief) {
        this.uvBrief = uvBrief;
    }

    public String getUvDetails() {
        return uvDetails;
    }

    public void setUvDetails(String uvDetails) {
        this.uvDetails = uvDetails;
    }

    public String getCarWashingBrief() {
        return carWashingBrief;
    }

    public void setCarWashingBrief(String carWashingBrief) {
        this.carWashingBrief = carWashingBrief;
    }

    public String getCarWashingDetails() {
        return carWashingDetails;
    }

    public void setCarWashingDetails(String carWashingDetails) {
        this.carWashingDetails = carWashingDetails;
    }

    public String getTravelBrief() {
        return travelBrief;
    }

    public void setTravelBrief(String travelBrief) {
        this.travelBrief = travelBrief;
    }

    public String getTravelDetails() {
        return travelDetails;
    }

    public void setTravelDetails(String travelDetails) {
        this.travelDetails = travelDetails;
    }

    public String getFluBrief() {
        return fluBrief;
    }

    public void setFluBrief(String fluBrief) {
        this.fluBrief = fluBrief;
    }

    public String getFluDetails() {
        return fluDetails;
    }

    public void setFluDetails(String fluDetails) {
        this.fluDetails = fluDetails;
    }

    public String getSportBrief() {
        return sportBrief;
    }

    public void setSportBrief(String sportBrief) {
        this.sportBrief = sportBrief;
    }

    public String getSportDetails() {
        return sportDetails;
    }

    public void setSportDetails(String sportDetails) {
        this.sportDetails = sportDetails;
    }

    public List<FutureBean> getFuture() {
        return future;
    }

    public void setFuture(List<FutureBean> future) {
        this.future = future;
    }
}
