package me.lancer.pocket.info.mvp.joke;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.util.ContentGetterSetter;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class JokeModel {

    IJokePresenter presenter;

    ContentGetterSetter contentGetterSetter = new ContentGetterSetter();
    String jokeUrl = "http://iu.snssdk.com/neihan/stream/mix/v1/?content_type=-102&count=50";
    String imageUrl = "http://iu.snssdk.com/neihan/stream/mix/v1/?content_type=-103&count=50";
    String videoUrl = "http://iu.snssdk.com/neihan/stream/mix/v1/?content_type=-104&count=50";

    public JokeModel(IJokePresenter presenter) {
        this.presenter = presenter;
    }

    public void loadText() {
        String content = contentGetterSetter.getContentFromHtml("Joke.loadJoke", jokeUrl);
        List<JokeBean> list;
        if (!content.contains("获取失败!")) {
            list = getJokeFromContent(0, content);
            presenter.loadTextSuccess(list);
        } else {
            presenter.loadTextFailure(content);
        }
    }

    public void loadImage() {
        String content = contentGetterSetter.getContentFromHtml("Joke.loadImage", imageUrl);
        List<JokeBean> list;
        if (!content.contains("获取失败!")) {
            list = getJokeFromContent(1, content);
            presenter.loadImageSuccess(list);
        } else {
            presenter.loadImageFailure(content);
        }
    }

    public void loadVideo() {
        String content = contentGetterSetter.getContentFromHtml("Joke.loadVideo", videoUrl);
        List<JokeBean> list;
        if (!content.contains("获取失败!")) {
            list = getJokeFromContent(2, content);
            presenter.loadVideoSuccess(list);
        } else {
            presenter.loadVideoFailure(content);
        }
    }

    public List<JokeBean> getJokeFromContent(int type, String content) {
        try {
            List<JokeBean> list = new ArrayList<>();
            JSONObject jsonObj = new JSONObject(content);
            if (jsonObj.getString("message").equals("success")) {
                JSONObject data = jsonObj.getJSONObject("data");
                JSONArray jsonArr = data.getJSONArray("data");
                for (int i = 0; i < jsonArr.length(); i++) {
                    JokeBean bean = new JokeBean();
                    JSONObject jbItem = jsonArr.getJSONObject(i);
                    if (jbItem.has("group")) {
                        bean.setType(type);
                        if (type == 0) {
                            bean.setText(jbItem.getJSONObject("group").getString("text"));
                        }
                        if (type == 1) {
                            if (jbItem.getJSONObject("group").has("large_image")) {
                                bean.setImg(jbItem.getJSONObject("group").getJSONObject("large_image").getJSONArray("url_list").getJSONObject(2).getString("url"));
                            } else if (jbItem.getJSONObject("group").has("large_image_list")) {
                                bean.setImg(jbItem.getJSONObject("group").getJSONObject("large_image_list").getJSONArray("url_list").getJSONObject(2).getString("url"));
                            }
                            bean.setText(jbItem.getJSONObject("group").getString("text"));
                        }
                        if (type == 2) {
                            if (jbItem.getJSONObject("group").has("mp4_url")) {
                                bean.setVideo(jbItem.getJSONObject("group").getString("mp4_url"));
                            }
                            bean.setText(jbItem.getJSONObject("group").getString("text"));
                        }
                        list.add(bean);
                    }
                }
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
