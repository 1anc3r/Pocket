package me.lancer.pocket.tool.mvp.calendar.bean;

/**
 * Created by HuangFangzhi on 2016/12/14.
 */

public class CalendarBean {

    private int time;
    private int day;
    private int color;
    private int length;
    private String name;
    private String location;

    public CalendarBean(){

    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
