package me.lancer.pocket.tool.mvp.weather;

/**
 * Created by HuangFangzhi on 2017/6/16.
 */

public class CityBean {

    private String id;
    private String cityName;
    private String townName;

    public CityBean() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }
}
