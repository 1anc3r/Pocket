package me.lancer.pocket.tool.mvp.translation;

/**
 * Created by HuangFangzhi on 2017/6/16.
 */

public class TranslationBean {

    private String pos;//词性
    private String d;//释义
    private String sd;//例句
    private String td;//例句翻译

    public TranslationBean() {

    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getSd() {
        return sd;
    }

    public void setSd(String sd) {
        this.sd = sd;
    }

    public String getTd() {
        return td;
    }

    public void setTd(String td) {
        this.td = td;
    }
}
