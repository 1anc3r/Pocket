package me.lancer.pocket.info.mvp.novel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.util.ContentGetterSetter;

/**
 * Created by HuangFangzhi on 2017/5/25.
 */

public class NovelModel {

    INovelPresenter presenter;

    ContentGetterSetter contentGetterSetter = new ContentGetterSetter();
    String rankUrl = "http://api.zhuishushenqi.com/ranking/";//不同性别下的榜单http://api.zhuishushenqi.com/ranking/gender
    String catsUrl = "http://api.zhuishushenqi.com/cats";//指定性别下的分类http://api.zhuishushenqi.com/cats/lv2?gender=male&type=new
    String catsHeadUrl = "http://api.zhuishushenqi.com/book/by-categories?gender=";
    String majorUrl = "&major=";
    String startUrl = "&start=";
    String limitUrl = "&limit=";//指定性别下的分类http://api.zhuishushenqi.com/book/by-categories?gender=male&type=hot&major=%E7%8E%84%E5%B9%BB&start=0&limit=50
    String searchUrl = "http://api.zhuishushenqi.com/book/fuzzy-search?query=";//搜索http://api.zhuishushenqi.com/book/fuzzy-search?query=一念
    String detailUrl = "http://api.zhuishushenqi.com/book/";//图书详情http://api.zhuishushenqi.com/book/573d65ab608bed412452ba69
    String sourceUrl = "http://api.zhuishushenqi.com/toc?view=summary&book=";//书源http://api.zhuishushenqi.com/toc?view=summary&book=573d65ab608bed412452ba69
    String chapterHeadUrl = "http://api.zhuishushenqi.com/toc/";//章节列表http://api.zhuishushenqi.com/toc/57e9357fbf649ec11272de11?view=chapters
    String chapterTailUrl = "?view=chapters";//章节列表http://api.zhuishushenqi.com/toc/57e9357fbf649ec11272de11?view=chapters
    String pagerHeadUrl = "http://chapter2.zhuishushenqi.com/chapter/";//章节内容http://chapter2.zhuishushenqi.com/chapter/http%3a%2f%2fbook.my716.com%2fgetBooks.aspx%3fmethod%3dcontent%26bookId%3d1127281%26chapterFile%3dU_1212539_201701211420571844_4093_2.txt?k=2124b73d7e2e1945&t=1468223717
    String pagerTailUrl = "?k=2124b73d7e2e1945&t=1468223717";//章节内容http://chapter2.zhuishushenqi.com/chapter/http%3a%2f%2fbook.my716.com%2fgetBooks.aspx%3fmethod%3dcontent%26bookId%3d1127281%26chapterFile%3dU_1212539_201701211420571844_4093_2.txt?k=2124b73d7e2e1945&t=1468223717
    String coverUrl = "http://statics.zhuishushenqi.com";//封皮http://statics.zhuishushenqi.com/agent/http://images.zhulang.com/book_cover/image/18/98/189843.jpg

    public NovelModel(INovelPresenter presenter) {
        this.presenter = presenter;
    }

    public void loadRankList() {
        String content = contentGetterSetter.getContentFromHtml("Novel.loadRank", rankUrl);
        List<NovelBean> list;
        if (!content.contains("获取失败!")) {
            list = getRankListFromContent(content);
            presenter.loadRankSuccess(list);
        } else {
            presenter.loadRankFailure(content);
        }
    }

    public void loadRankItem(String id) {
        String content = contentGetterSetter.getContentFromHtml("Novel.loadRank", rankUrl + id);
        List<NovelBean> list;
        if (!content.contains("获取失败!")) {
            list = getRankItemFromContent(content);
            presenter.loadRankSuccess(list);
        } else {
            presenter.loadRankFailure(content);
        }
    }

    public void loadCateList() {
        String content = contentGetterSetter.getContentFromHtml("Novel.loadCate", catsUrl);
        List<NovelBean> list;
        if (!content.contains("获取失败!")) {
            list = getCateListFromContent(content);
            presenter.loadCateSuccess(list);
        } else {
            presenter.loadCateFailure(content);
        }
    }

    public void loadCateItem(String gender, String major, int start, int limit) {
        if (gender.equals("男生")) {
            gender = "male";
        } else if (gender.equals("女生")) {
            gender = "female";
        }
        String content = contentGetterSetter.getContentFromHtml("Novel.loadCate", catsHeadUrl + gender + majorUrl + major + startUrl + start + limitUrl + limit);
        List<NovelBean> list;
        if (!content.contains("获取失败!")) {
            list = getCateItemFromContent(content);
            presenter.loadCateSuccess(list);
        } else {
            presenter.loadCateFailure(content);
        }
    }

    public void loadSearch(String query) {
        String content = contentGetterSetter.getContentFromHtml("Novel.loadSearch", searchUrl + query);
        List<NovelBean> list;
        if (!content.contains("获取失败!")) {
            list = getListFromContent(content);
            presenter.loadSearchSuccess(list);
        } else {
            presenter.loadSearchFailure(content);
        }
    }

    public void loadDetail(String id) {
        String content = contentGetterSetter.getContentFromHtml("Novel.loadNovel", detailUrl + id);
        NovelBean bean;
        if (!content.contains("获取失败!")) {
            bean = getDetailFromContent(content);
            presenter.loadDetailSuccess(bean);
        } else {
            presenter.loadDetailFailure(content);
        }
    }

    public void switchSource(String bookId) {
        String content = contentGetterSetter.getContentFromHtml("Novel.switchSource", sourceUrl + bookId);
        String sourceId = "";
        if (!content.contains("获取失败!")) {
            sourceId = getSourceIdFromContent(content);
            presenter.switchSourceSuccess(sourceId);
        } else {
            presenter.switchSourceFailure(content);
        }
    }

    public void loadChapterList(String id) {
        String content = contentGetterSetter.getContentFromHtml("Novel.loadChapterList", chapterHeadUrl + id + chapterTailUrl);
        List<NovelBean.Chapters> list;
        if (!content.contains("获取失败!")) {
            list = getChaptersFromContent(content);
            presenter.loadChapterSuccess(list);
        } else {
            presenter.loadChapterFailure(content);
        }
    }

    private List<NovelBean> getRankListFromContent(String content) {
        try {
            List<NovelBean> list = new ArrayList<>();
            JSONObject all = new JSONObject(content);
            JSONArray ranking = all.getJSONArray("rankings");
            for (int i = 0; i < ranking.length(); i++) {
                NovelBean item = new NovelBean();
                JSONObject jsonItm = ranking.getJSONObject(i);
                item.setId(jsonItm.getString("_id"));
                item.setTitle(jsonItm.getString("title"));
                item.setCover(coverUrl + jsonItm.getString("cover"));
                item.setType(0);
                list.add(item);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<NovelBean> getRankItemFromContent(String content) {
        try {
            List<NovelBean> list = new ArrayList<>();
            JSONObject all = new JSONObject(content);
            JSONObject ranking = all.getJSONObject("ranking");
            JSONArray books = ranking.getJSONArray("books");
            for (int i = 0; i < books.length(); i++) {
                NovelBean item = new NovelBean();
                JSONObject jsonItm = books.getJSONObject(i);
                item.setId(jsonItm.getString("_id"));
                item.setTitle(jsonItm.getString("title"));
                item.setAuthor(jsonItm.getString("author"));
                item.setIntro(jsonItm.getString("shortIntro"));
                item.setCover(coverUrl + jsonItm.getString("cover"));
                item.setCount(jsonItm.getInt("latelyFollower"));
                item.setRatio(jsonItm.getString("retentionRatio"));
                item.setType(0);
                list.add(item);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<NovelBean> getCateListFromContent(String content) {
        try {
            List<NovelBean> list = new ArrayList<>();
            JSONObject all = new JSONObject(content);
            list.add(new NovelBean("男生", 1));
            JSONArray male = all.getJSONArray("male");
            for (int i = 0; i < male.length(); i++) {
                NovelBean item = new NovelBean();
                JSONObject jsonItm = male.getJSONObject(i);
                item.setId("男生");
                item.setTitle(jsonItm.getString("name"));
                item.setCount(jsonItm.getInt("bookCount"));
                item.setType(0);
                list.add(item);
            }
            list.add(new NovelBean("女生", 1));
            JSONArray female = all.getJSONArray("male");
            for (int i = 0; i < female.length(); i++) {
                NovelBean item = new NovelBean();
                JSONObject jsonItm = female.getJSONObject(i);
                item.setId("女生");
                item.setTitle(jsonItm.getString("name"));
                item.setCount(jsonItm.getInt("bookCount"));
                item.setType(0);
                list.add(item);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<NovelBean> getCateItemFromContent(String content) {
        try {
            List<NovelBean> list = new ArrayList<>();
            JSONObject all = new JSONObject(content);
            JSONArray books = all.getJSONArray("books");
            for (int i = 0; i < books.length(); i++) {
                NovelBean item = new NovelBean();
                JSONObject jsonItm = books.getJSONObject(i);
                item.setId(jsonItm.getString("_id"));
                item.setTitle(jsonItm.getString("title"));
                item.setAuthor(jsonItm.getString("author"));
                item.setIntro(jsonItm.getString("shortIntro"));
                item.setCover(coverUrl + jsonItm.getString("cover"));
                item.setCount(jsonItm.getInt("latelyFollower"));
                item.setRatio(jsonItm.getString("retentionRatio"));
                item.setType(0);
                list.add(item);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<NovelBean> getListFromContent(String content) {
        try {
            List<NovelBean> list = new ArrayList<>();
            JSONObject all = new JSONObject(content);
            JSONArray books = all.getJSONArray("books");
            for (int i = 0; i < books.length(); i++) {
                NovelBean item = new NovelBean();
                JSONObject jsonItm = books.getJSONObject(i);
                item.setId(jsonItm.getString("_id"));
                item.setTitle(jsonItm.getString("title"));
                item.setAuthor(jsonItm.getString("author"));
                item.setIntro(jsonItm.getString("shortIntro"));
                item.setCover((coverUrl + jsonItm.getString("cover")).replace("%3A", ":").replace("%2F", "/").replace("%26", "&").replace("%5B", "[").replace("%5D", "]"));
                item.setCount(jsonItm.getInt("latelyFollower"));
                item.setRatio(jsonItm.getString("retentionRatio"));
                item.setType(0);
                list.add(item);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private NovelBean getDetailFromContent(String content) {
        try {
            NovelBean bean = new NovelBean();
            JSONObject all = new JSONObject(content);
            bean.setId(all.getString("_id"));
            bean.setTitle(all.getString("title"));
            bean.setAuthor(all.getString("author"));
            bean.setIntro(all.getString("longIntro"));
            String category = "";
            JSONArray tags = all.getJSONArray("tags");
            for (int i = 0; i < tags.length(); i++) {
                category += tags.getString(i) + ";";
            }
            bean.setCategory(category);
            bean.setCover(coverUrl + all.getString("cover"));
            bean.setCount(all.getInt("wordCount"));
            bean.setRatio(all.getString("retentionRatio"));
            bean.setType(0);
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getSourceIdFromContent(String content) {
        try {
            JSONArray all = new JSONArray(content);
            for (int i = 0; i < all.length(); i++) {
                JSONObject jsonItm = all.getJSONObject(i);
                if (!jsonItm.getString("name").equals("优质书源")) {
                    return jsonItm.getString("_id");
                }
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<NovelBean.Chapters> getChaptersFromContent(String content) {
        try {
            List<NovelBean.Chapters> list = new ArrayList<>();
            JSONObject all = new JSONObject(content);
            JSONArray chapters = all.getJSONArray("chapters");
            for (int i = 0; i < chapters.length(); i++) {
                JSONObject jsonItm = chapters.getJSONObject(i);
                String title = jsonItm.getString("title");
                String link = pagerHeadUrl + jsonItm.getString("link") + pagerTailUrl;
                NovelBean.Chapters item = new NovelBean.Chapters(title, link);
                list.add(item);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
