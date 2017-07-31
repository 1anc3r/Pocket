package me.lancer.pocket.ui.model;

/**
 * Created by HuangFangzhi on 2017/6/13.
 */

public class ModelBean {

    private String name;
    private int image;

    public ModelBean() {
    }

    public ModelBean(String name, int image) {
        this.name = name;
        this.image = image;
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
