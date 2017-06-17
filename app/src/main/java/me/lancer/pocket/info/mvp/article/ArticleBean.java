package me.lancer.pocket.info.mvp.article;

/**
 * Created by HuangFangzhi on 2017/6/15.
 */

public class ArticleBean {

    private String title;
    private String author;
    private String digest;
    private String content;
    private int words;

    public ArticleBean(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }
}
