package me.lancer.pocket.info.mvp.app;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.url.APP_URL;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class AppModel {

    IAppPresenter presenter;

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
        List<AppBean> list;
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
                list = getAppListFromContent(content.toString());
                presenter.loadSearchSuccess(list);
            } else {
                presenter.loadSearchFailure("获取失败!");
            }
        } catch (Exception e) {
            presenter.loadSearchFailure("获取失败!捕获异常:" + e.toString());
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
                presenter.loadDetailSuccess(getDetailFromContent(content.toString()));
            } else {
                presenter.loadDetailFailure("获取失败!");
            }
        } catch (Exception e) {
            presenter.loadDetailFailure("获取失败!捕获异常:" + e.toString());
        }
    }

    private List<AppBean> getAppListFromContent(String content) {
        try {
            List<AppBean> list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(content);
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
        try {
            AppBean bean = new AppBean();
            JSONObject jsonObject = new JSONObject(content);
            JSONObject field = jsonObject.getJSONObject("field");
            bean.setId(field.getString("aid"));
            bean.setPkgName(field.getString("apkname"));
            bean.setVersCode(field.getInt("apkversioncode"));
            bean.setVersNum(field.getString("apkversionname"));
            bean.setVersLog(field.getString("changelog"));
            bean.setSupport(field.getString("romversion"));
            bean.setIntro(field.getString("introduce"));
            bean.setRemark(field.getString("remark"));
            bean.setLanguage(field.getString("language"));
            bean.setScreenshots(field.getString("screenshot").split(","));
            String apkfile = field.getString("apkfile");
            if (apkfile.contains("http")) {
                bean.setDownLink(apkfile);
            } else {
                bean.setDownLink(APP_URL.DOWN_URL + apkfile);
            }
            JSONObject meta = jsonObject.getJSONObject("meta");
            bean.setApkName(meta.getString("title"));
            bean.setLogo(meta.getString("logo"));
            bean.setAuthor(meta.getString("developername"));
            bean.setDownNum(meta.getString("downnum"));
            bean.setFavrNum(meta.getString("favnum"));
            bean.setCommNum(meta.getString("commentnum"));
            bean.setStar(meta.getString("score"));
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
