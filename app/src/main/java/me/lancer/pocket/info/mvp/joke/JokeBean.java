package me.lancer.pocket.info.mvp.joke;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class JokeBean {

    private int type;
    private String text;
    private String img;
    private String video;

    public JokeBean() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
