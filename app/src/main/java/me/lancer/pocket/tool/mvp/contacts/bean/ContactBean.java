package me.lancer.pocket.tool.mvp.contacts.bean;

import java.util.List;

/**
 * Created by HuangFangzhi on 2017/6/14.
 */

public class ContactBean {

    private String name;
    private String number;
    private int type;
    private String date;
    private String duration;
    private List<MessageBean> msgs;

    public ContactBean(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<MessageBean> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<MessageBean> msgs) {
        this.msgs = msgs;
    }
}
