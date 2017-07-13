package me.lancer.pocket.info.mvp.novel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HuangFangzhi on 2017/5/25.
 */

public class NovelBean implements Serializable {

    private String id;
    private String author;
    private String title;
    private String intro;
    private String category;
    private String tags;
    private int count;
    private String ratio;
    private String updated;
    private String cover;
    private String link;
    private int type;

    public List<Chapters> chapters;

    public static class Chapters implements Serializable {
        public String title;
        public String link;

        public Chapters() {
        }

        public Chapters(String title, String link) {
            this.title = title;
            this.link = link;
        }
    }

    public NovelBean() {

    }

    public NovelBean(String title, int type) {
        this.title = title;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
