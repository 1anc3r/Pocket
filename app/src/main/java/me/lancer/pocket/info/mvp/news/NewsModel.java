package me.lancer.pocket.info.mvp.news;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.lancer.pocket.util.ContentGetterSetter;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class NewsModel {

    INewsPresenter presenter;

    ContentGetterSetter contentGetterSetter = new ContentGetterSetter();
    String newsUrl = "http://news-at.zhihu.com/api/4/news/";
    String latestUrl = "http://news-at.zhihu.com/api/4/news/latest";
    String beforeUrl = "http://news-at.zhihu.com/api/4/news/before/";
    String hotestUrl = "http://news-at.zhihu.com/api/3/news/hot";
    String publicUrl = "http://v.juhe.cn/weixin/query?key=26ce25ffcfc907a26263e2b0e3e23676&pno=%d&ps=21";
    String itemUrl = "http://news-at.zhihu.com/api/4/theme/";
    String listUrl = "http://news-at.zhihu.com/api/4/themes";
    Comparator<NewsBean> comparator = new Comparator<NewsBean>() {
        @Override
        public int compare(NewsBean nb1, NewsBean nb2) {
            if (nb1.getId() > nb2.getId()) {
                return 1;
            } else {
                return -1;
            }
        }
    };

    public NewsModel(INewsPresenter presenter) {
        this.presenter = presenter;
    }

    public void loadLatest() {
        String content = contentGetterSetter.getContentFromHtml("News.loadLatest", latestUrl);
        List<NewsBean> list;
        if (!content.contains("获取失败!")) {
            list = getLatestNewsFromContent(content);
            presenter.loadLatestSuccess(list);
        } else {
            presenter.loadLatestFailure(content);
        }
    }

    public void loadBefore(String date) {
        String content = contentGetterSetter.getContentFromHtml("News.loadBefore", beforeUrl + date);
        List<NewsBean> list;
        if (!content.contains("获取失败!")) {
            list = getLatestNewsFromContent(content);
            presenter.loadBeforeSuccess(list);
        } else {
            presenter.loadBeforeFailure(content);
        }
    }

    public void loadHotest() {
        String content = contentGetterSetter.getContentFromHtml("News.loadHotest", hotestUrl);
        List<NewsBean> list;
        if (!content.contains("获取失败!")) {
            list = getHotestNewsFromContent(content);
            presenter.loadHotestSuccess(list);
        } else {
            presenter.loadHotestFailure(content);
        }
    }

    public void loadPublic(int page) {
        String content = contentGetterSetter.getContentFromHtml("News.loadPublic", String.format(publicUrl, page));
        List<NewsBean> list;
        if (!content.contains("获取失败!")) {
            list = getPublicNewsFromContent(content);
            presenter.loadPublicSuccess(list);
        } else {
            presenter.loadPublicFailure(content);
        }
    }

    public void loadList() {
        String content = contentGetterSetter.getContentFromHtml("News.loadList", listUrl);
        List<NewsBean> list;
        if (!content.contains("获取失败!")) {
            list = getListNewsFromContent(content);
            presenter.loadListSuccess(list);
        } else {
            presenter.loadListFailure(content);
        }
    }

    public void loadItem(int type) {
        String content = contentGetterSetter.getContentFromHtml("News.loadItem", itemUrl + type);
        List<NewsBean> list;
        if (!content.contains("获取失败!")) {
            list = getItemNewsFromContent(content);
            presenter.loadItemSuccess(list);
        } else {
            presenter.loadItemFailure(content);
        }
    }

    public void loadDetail(String url) {
        String content = contentGetterSetter.getContentFromHtml("News.loadDetail", url);
        NewsBean bean;
        if (!content.contains("获取失败!")) {
            bean = getDetailFromContent(content);
            presenter.loadDetailSuccess(bean);
        } else {
            presenter.loadDetailFailure(content);
        }
    }

    public List<NewsBean> getLatestNewsFromContent(String content) {
        try {
            List<NewsBean> list = new ArrayList<>();
            JSONObject jbNews = new JSONObject(content);
            JSONArray jaNews = jbNews.getJSONArray("stories");
            for (int i = 0; i < jaNews.length(); i++) {
                NewsBean bean = new NewsBean();
                JSONObject jbItem = jaNews.getJSONObject(i);
                bean.setId(jbItem.getInt("id"));
                if (i == 0) {
                    bean.setType(-1);
                } else {
                    bean.setType(0);
                }
                bean.setTitle(jbItem.getString("title"));
                JSONArray jaImg = jbItem.getJSONArray("images");
                bean.setImg(jaImg.get(0).toString());
                bean.setLink(newsUrl + bean.getId());
                list.add(bean);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<NewsBean> getHotestNewsFromContent(String content) {
        try {
            List<NewsBean> list = new ArrayList<>();
            JSONObject jbNews = new JSONObject(content);
            JSONArray jaNews = jbNews.getJSONArray("recent");
            for (int i = 0; i < jaNews.length(); i++) {
                NewsBean bean = new NewsBean();
                JSONObject jbItem = jaNews.getJSONObject(i);
                bean.setId(jbItem.getInt("news_id"));
                bean.setType(0);
                bean.setTitle(jbItem.getString("title"));
                bean.setImg(jbItem.getString("thumbnail"));
                bean.setLink(jbItem.getString("url"));
                list.add(bean);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<NewsBean> getPublicNewsFromContent(String content) {
        try {
            List<NewsBean> list = new ArrayList<>();
            JSONObject all = new JSONObject(content);
            int errorCode = all.getInt("error_code");
            String reason = all.getString("reason");
            if (errorCode == 0 && reason.equals("请求成功")) {
                JSONObject result = all.getJSONObject("result");
                JSONArray jist = result.getJSONArray("list");
                for (int i = 0; i < jist.length(); i++) {
                    NewsBean bean = new NewsBean();
                    JSONObject jbItem = jist.getJSONObject(i);
                    bean.setId(-1);
                    bean.setType(0);
                    bean.setTitle(jbItem.getString("title"));
                    bean.setImg(jbItem.getString("firstImg"));
                    bean.setLink(jbItem.getString("url"));
                    list.add(bean);
                }
                return list;
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<NewsBean> getListNewsFromContent(String content) {
        try {
            List<NewsBean> list = new ArrayList<>();
            JSONObject jbNews = new JSONObject(content);
            JSONArray jaNews = jbNews.getJSONArray("others");
            for (int i = 0; i < jaNews.length(); i++) {
                NewsBean bean = new NewsBean();
                JSONObject jbItem = jaNews.getJSONObject(i);
                bean.setId(jbItem.getInt("id"));
                bean.setType(0);
                bean.setTitle(jbItem.getString("name"));
                bean.setImg(jbItem.getString("thumbnail"));
                list.add(bean);
            }
            Collections.sort(list, comparator);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<NewsBean> getItemNewsFromContent(String content) {
        try {
            List<NewsBean> list = new ArrayList<>();
            JSONObject jbNews = new JSONObject(content);
            JSONArray jaNews = jbNews.getJSONArray("stories");
            for (int i = 0; i < jaNews.length(); i++) {
                NewsBean bean = new NewsBean();
                JSONObject jbItem = jaNews.getJSONObject(i);
                if (!jbItem.isNull("images")) {
                    bean.setId(jbItem.getInt("id"));
                    bean.setType(0);
                    bean.setTitle(jbItem.getString("title"));
                    JSONArray jaImg = jbItem.getJSONArray("images");
                    bean.setImg(jaImg.get(0).toString());
                    bean.setLink(newsUrl + bean.getId());
                    list.add(bean);
                }
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public NewsBean getDetailFromContent(String content) {
        try {
            NewsBean bean = new NewsBean();
            JSONObject jbNews = new JSONObject(content);
            bean.setId(jbNews.getInt("id"));
            if (jbNews.has("type")) {
                bean.setType(jbNews.getInt("type"));
            }
            bean.setTitle(jbNews.getString("title"));
            if (jbNews.has("image")) {
                bean.setImg(jbNews.getString("image"));
            } else if (jbNews.has("images")) {
                JSONArray jaImg = jbNews.getJSONArray("images");
                bean.setImg(jaImg.get(0).toString());
            }
            bean.setContent(jbNews.getString("body").replace("\\n", ""));
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
