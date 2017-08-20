package me.lancer.pocket.info.mvp.photo;

import android.os.Environment;
import android.util.Log;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.lancer.pocket.util.ContentGetterSetter;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class PhotoModel {

    IPhotoPresenter presenter;

    ContentGetterSetter contentGetterSetter = new ContentGetterSetter();
    String imgPexelsUrl = "https://www.pexels.com/?page=";
    String imgGankUrl = "http://gank.io/api/data/福利/10/";
    String imgDetailUrlPexels = "?w=940&h=650&auto=compress&cs=tinysrgb";
    String imgDetailUrlHuaban = "http://img.hb.aicdn.com/";
    String[] imgHuanBanUrls = {
            "http://api.huaban.com/favorite/anime?limit=42&max=",//动漫
            "http://api.huaban.com/favorite/games?limit=42&max=",//游戏
            "http://api.huaban.com/favorite/film_music_books?limit=42&max=",//电影/音乐/图书
            "http://api.huaban.com/favorite/quotes?limit=42&max=",//唯美
            "http://api.huaban.com/favorite/photography?limit=42&max=",//摄影
            "http://api.huaban.com/favorite/travel_places?limit=42&max=",//旅行
            "http://api.huaban.com/favorite/pets?limit=42&max=",//宠物
            "http://api.huaban.com/favorite/kids?limit=42&max=",//小孩
            "http://api.huaban.com/favorite/beauty?limit=42&max=",//美女
            "http://api.huaban.com/favorite/apparel?limit=42&max=",//女装/搭配
            "http://api.huaban.com/favorite/men?limit=42&max=",//男士/风尚
            "http://api.huaban.com/favorite/modeling_hair?limit=42&max=",//造型/美妆
    };

    public PhotoModel(IPhotoPresenter presenter) {
        this.presenter = presenter;
    }

    public void download(String url, String title) {
        String path = Environment.getExternalStorageDirectory().toString();
        OkHttpClient client = new OkHttpClient();
        client.setFollowRedirects(false);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                File dir = new File(path + "/me.lancer.pocket.info");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir.getPath() + "/" + title + ".jpeg");
                InputStream is = response.body().byteStream();
                OutputStream os = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int c;
                while ((c = is.read(b)) > 0) {
                    os.write(b, 0, c);
                }
                is.close();
                os.close();
                presenter.downloadSuccess("下载成功!图片路径:" + file.getPath());
            } else {
                presenter.downloadFailure("下载失败!状态码:" + response.code());
            }
        } catch (IOException e) {
            presenter.downloadFailure("下载失败!捕获异常:" + e.toString());
        }
    }

    public void loadPexels(int pager) {
        String content = contentGetterSetter.getContentFromHtml("Photo.loadLatest", imgPexelsUrl + pager);
        List<PhotoBean> list;
        if (!content.contains("获取失败!")) {
            list = getPexelsFromHtmlContent(content);
            presenter.loadPexelsSuccess(list);
        } else {
            presenter.loadPexelsFailure(content);
        }
    }

    public void loadGank(int pager) {
        String content = contentGetterSetter.getContentFromHtml("Photo.loadGank", imgGankUrl + pager);
        List<PhotoBean> list;
        if (!content.contains("获取失败!")) {
            list = getGankFromJsonContent(content);
            presenter.loadGankSuccess(list);
        } else {
            presenter.loadGankFailure(content);
        }
    }

    public void loadHuaban(int type, String max) {
        String content = contentGetterSetter.getContentFromHtml("Photo.loadHuaban", imgHuanBanUrls[type] + max);
        List<PhotoBean> list;
        if (!content.contains("获取失败!")) {
            list = getHuabanFromJsonContent(content);
            presenter.loadHuabanSuccess(list);
        } else {
            presenter.loadHuabanFailure(content);
        }
    }

    public List<PhotoBean> getPexelsFromHtmlContent(String content) {
        List<PhotoBean> list = new ArrayList<>();
        Document document = Jsoup.parse(content);
        Elements elements = document.getElementsByTag("img");
        for (Element element : elements) {
            PhotoBean bean = new PhotoBean();
            String imgSmall = element.getElementsByTag("img").attr("src");
            if (imgSmall.contains("auto=compress&cs=tinysrgb")) {
                bean.setType(new Random().nextInt(3) % (3 - 1 + 1) + 1);
                bean.setTitle(imgSmall.substring(0, imgSmall.indexOf('?')).replace("https://images.pexels.com/photos/", "").substring(0, imgSmall.indexOf('/')).replace("/", ""));
                bean.setImgSmall(imgSmall);
                bean.setImgLarge(imgSmall);
//                bean.setImgLarge(imgSmall.substring(0, imgSmall.indexOf('?')) + imgDetailUrlPexels);
                list.add(bean);
            }
        }
        return list;
    }

    public List<PhotoBean> getGankFromJsonContent(String content) {
        try {
            List<PhotoBean> list = new ArrayList<>();
            JSONObject jbPhoto = new JSONObject(content);
            if (!jbPhoto.getBoolean("error")) {
                JSONArray jaPhoto = jbPhoto.getJSONArray("results");
                for (int i = 0; i < jaPhoto.length(); i++) {
                    JSONObject jbItem = (JSONObject) jaPhoto.get(i);
                    PhotoBean bean = new PhotoBean();
                    bean.setType(new Random().nextInt(3) % (3 - 1 + 1) + 1);
                    bean.setTitle(jbItem.getString("_id"));
                    bean.setImgLarge(jbItem.getString("url"));
                    bean.setImgSmall(jbItem.getString("url"));
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

    public List<PhotoBean> getHuabanFromJsonContent(String content) {
        try {
            List<PhotoBean> list = new ArrayList<>();
            JSONObject all = new JSONObject(content);
            JSONArray pins = all.getJSONArray("pins");
            for (int i = 0; i < pins.length(); i++) {
                JSONObject jbItem = (JSONObject) pins.get(i);
                if (jbItem.has("pin_id") && jbItem.has("file")) {
                    PhotoBean bean = new PhotoBean();
                    bean.setType(new Random().nextInt(3) % (3 - 1 + 1) + 1);
                    bean.setTitle(String.valueOf(jbItem.getInt("pin_id")));
                    JSONObject file = jbItem.getJSONObject("file");
                    bean.setImgLarge(imgDetailUrlHuaban + file.getString("key"));
                    bean.setImgSmall(imgDetailUrlHuaban + file.getString("key"));
                    list.add(bean);
                }
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
