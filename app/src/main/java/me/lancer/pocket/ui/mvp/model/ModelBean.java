package me.lancer.pocket.ui.mvp.model;

import java.util.List;

import me.lancer.pocket.ui.mvp.collect.CollectBean;

/**
 * Created by HuangFangzhi on 2017/6/13.
 */

public class ModelBean {

    private int id;
    private String name;
    private int image;
    private List<CollectBean> list;

    public ModelBean() {
    }

    public ModelBean(int id, String name, int image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image_in) {
        this.image = image_in;
    }

    public List<CollectBean> getList() {
        return list;
    }

    public void setList(List<CollectBean> list) {
        this.list = list;
    }
}
