package me.lancer.pocket.info.mvp.app;

import android.graphics.drawable.Drawable;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class AppBean {

    private String id;//id
    private String apkName;//应用名
    private String pkgName;//包名
    private String apkSize;//大小
    private String author;
    private String logo;
    private Drawable icon;
    private int versCode;
    private String versNum;//版本号
    private String versLog;//版本介绍
    private String star;//评分
    private String downLink;//下载
    private String downNum;//下载（详）
    private String favrNum;//关注
    private String commNum;//评论
    private String publish;//上线日期
    private String update;//更新日期
    private String support;//支持手机系统版本
    private String intro;//简介
    private String remark;//小编点评
    private String language;//语言
    private String[] screenshots;//截图

    public AppBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getApkSize() {
        return apkSize;
    }

    public void setApkSize(String apkSize) {
        this.apkSize = apkSize;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getVersCode() {
        return versCode;
    }

    public void setVersCode(int versCode) {
        this.versCode = versCode;
    }

    public String getVersNum() {
        return versNum;
    }

    public void setVersNum(String versNum) {
        this.versNum = versNum;
    }

    public String getVersLog() {
        return versLog;
    }

    public void setVersLog(String versLog) {
        this.versLog = versLog;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getDownLink() {
        return downLink;
    }

    public void setDownLink(String downLink) {
        this.downLink = downLink;
    }

    public String getDownNum() {
        return downNum;
    }

    public void setDownNum(String downNum) {
        this.downNum = downNum;
    }

    public String getFavrNum() {
        return favrNum;
    }

    public void setFavrNum(String favrNum) {
        this.favrNum = favrNum;
    }

    public String getCommNum() {
        return commNum;
    }

    public void setCommNum(String commNum) {
        this.commNum = commNum;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String[] getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(String[] screenshots) {
        this.screenshots = screenshots;
    }

}
