package me.lancer.pocket.info.mvp.app;

import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.url.APP_URL;
import me.lancer.pocket.util.ContentGetterSetter;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class AppModel {

    IAppPresenter presenter;

    ContentGetterSetter contentGetterSetter = new ContentGetterSetter();

    public AppModel(IAppPresenter presenter) {
        this.presenter = presenter;
    }

    public void loadHomepage(int pager) {
        List<AppBean> list;
        String url = String.format(APP_URL.HOME_URL, pager);
        StringBuilder content = new StringBuilder();
        OkHttpClient client = new OkHttpClient();
        client.setFollowRedirects(false);
        Request request = new Request.Builder().url(url).addHeader("Cookie", APP_URL.COOKIE).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                BufferedReader reader = new BufferedReader(response.body().charStream());
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                list = getAppListFromContent(content.toString());
                presenter.loadHomepageSuccess(list);
            } else {
                presenter.loadHomepageFailure("获取失败!");
            }
        } catch (Exception e) {
            presenter.loadHomepageFailure("获取失败!捕获异常:" + e.toString());
        }
    }

    public void loadSearch(String query, int pager) {
        StringBuilder content = new StringBuilder();
        OkHttpClient client = new OkHttpClient();
        client.setFollowRedirects(false);
        Request request = new Request.Builder().url(String.format(APP_URL.SEARCH_URL, query, pager)).addHeader("Cookie", APP_URL.COOKIE).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                BufferedReader reader = new BufferedReader(response.body().charStream());
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                presenter.loadHomepageSuccess(getAppListFromContent(content.toString()));
            } else {
                presenter.loadHomepageFailure("获取失败!");
            }
        } catch (Exception e) {
            presenter.loadHomepageFailure("获取失败!捕获异常:" + e.toString());
        }
    }

    public void loadDetail(String id) {
        StringBuilder content = new StringBuilder();
        OkHttpClient client = new OkHttpClient();
        client.setFollowRedirects(false);
        Request request = new Request.Builder().url(String.format(APP_URL.DETAIL_URL, id)).addHeader("Cookie", APP_URL.COOKIE).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                BufferedReader reader = new BufferedReader(response.body().charStream());
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                presenter.loadHomepageSuccess(getAppListFromContent(content.toString()));
            } else {
                presenter.loadHomepageFailure("获取失败!");
            }
        } catch (Exception e) {
            presenter.loadHomepageFailure("获取失败!捕获异常:" + e.toString());
        }
    }

    private List<AppBean> getAppListFromContent(String content) {
        try {
            List<AppBean> list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(content);
            int count = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                AppBean bean = new AppBean();
                JSONObject jsonItm = jsonArray.getJSONObject(i);
                bean.setId(jsonItm.getString("id"));
                bean.setApkName(jsonItm.getString("title"));
                bean.setLogo(jsonItm.getString("logo"));
                list.add(bean);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private AppBean getDetailFromContent(String content) {
        Log.e("getDetailFromContent: ", content);
        return null;
    }
}
