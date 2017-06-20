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
    String imgListUrl = "https://www.pexels.com/?page=";
    String imgThemeUrl = "https://www.pexels.com/search/";
    String imgWelfareUrl = "http://gank.io/api/data/福利/10/";
    String imgDetailUrl = "?w=940&h=650&auto=compress&cs=tinysrgb";

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

    public void loadLatest(int pager) {
        String content = contentGetterSetter.getContentFromHtml("Photo.loadLatest", imgListUrl + pager);
        List<PhotoBean> list;
        if (!content.contains("获取失败!")) {
            list = getPhotosFromHtmlContent(content);
            presenter.loadLatestSuccess(list);
        } else {
            presenter.loadLatestFailure(content);
        }
    }

    public void loadTheme(String type) {
        String content = contentGetterSetter.getContentFromHtml("Photo.loadTheme", imgThemeUrl + type);
        List<PhotoBean> list;
        if (!content.contains("获取失败!")) {
            list = getPhotosFromHtmlContent(content);
            presenter.loadThemeSuccess(list);
        } else {
            presenter.loadThemeFailure(content);
        }
    }

    public void loadWelfare(int pager) {
        String content = contentGetterSetter.getContentFromHtml("Photo.loadWelfare", imgWelfareUrl + pager);
        List<PhotoBean> list;
        if (!content.contains("获取失败!")) {
            list = getPhotosFromJsonContent(content);
            presenter.loadWelfareSuccess(list);
        } else {
            presenter.loadWelfareFailure(content);
        }
    }

    public List<PhotoBean> getPhotosFromHtmlContent(String content) {
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
                bean.setImgLarge(imgSmall.substring(0, imgSmall.indexOf('?')) + imgDetailUrl);
                list.add(bean);
            }
        }
        return list;
    }

    public List<PhotoBean> getPhotosFromJsonContent(String content) {
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
}
