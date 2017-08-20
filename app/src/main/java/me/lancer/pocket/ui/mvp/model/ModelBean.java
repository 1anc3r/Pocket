package me.lancer.pocket.ui.mvp.model;

/**
 * Created by HuangFangzhi on 2017/6/13.
 */

public class ModelBean {

    private int id;
    private String name;
    private int image;

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
}
