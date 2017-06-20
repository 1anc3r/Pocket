package me.lancer.pocket.info.mvp.article;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import me.lancer.pocket.util.ContentGetterSetter;

/**
 * Created by HuangFangzhi on 2017/6/15.
 */

public class ArticleModel {

    IArticlePresenter presenter;

    ContentGetterSetter contentGetterSetter = new ContentGetterSetter();
    String dailyUrl = "https://interface.meiriyiwen.com/article/today?dev=1";
    String randomUrl = "https://interface.meiriyiwen.com/article/random?dev=1";

    public ArticleModel(IArticlePresenter presenter) {
        this.presenter = presenter;
    }

    public void loadDaily() {
        String content = contentGetterSetter.getContentFromHtml("Article.loadDaily", dailyUrl);
        ArticleBean bean;
        if (!content.contains("获取失败!")) {
            bean = getArticleFromContent(content);
            presenter.loadDailySuccess(bean);
        } else {
            presenter.loadDailyFailure(content);
        }
    }

    public void loadRandom() {
        String content = contentGetterSetter.getContentFromHtml("Article.loadRandom", randomUrl);
        ArticleBean bean;
        if (!content.contains("获取失败!")) {
            bean = getArticleFromContent(content);
            presenter.loadRandomSuccess(bean);
        } else {
            presenter.loadRandomFailure(content);
        }
    }

    private ArticleBean getArticleFromContent(String content) {
        try {
            ArticleBean bean = new ArticleBean();
            JSONObject obj = new JSONObject(content);
            JSONObject data = obj.getJSONObject("data");
            bean.setAuthor(data.getString("author"));
            bean.setTitle(data.getString("title"));
            bean.setDigest(data.getString("digest"));
            bean.setContent(data.getString("content"));
            bean.setWords(data.getInt("wc"));
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
