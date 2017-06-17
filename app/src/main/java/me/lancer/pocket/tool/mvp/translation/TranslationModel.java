package me.lancer.pocket.tool.mvp.translation;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.util.ContentGetterSetter;

/**
 * Created by HuangFangzhi on 2017/6/16.
 */

public class TranslationModel {

    ITranslationPresenter presenter;

    ContentGetterSetter contentGetterSetter = new ContentGetterSetter();
    String headUrl = "https://dict.bing.com.cn/api/http/v2/4154AA7A1FC54ad7A84A0236AA4DCAF3/en-us/zh-cn/?q=";
    String tailUrl = "&format=application/json";

    public TranslationModel(ITranslationPresenter presenter) {
        this.presenter = presenter;
    }

    public void loadTranslation(String keyword) {
        String content = contentGetterSetter.getContentFromHtml("Translation.loadTranslation", headUrl + keyword.replace(" ", "+") + tailUrl);
        List<TranslationBean> list;
        if (!content.contains("获取失败!")) {
            list = getTranslationFromContent(keyword, content);
            presenter.loadTranslationSuccess(list);
        } else {
            presenter.loadTranslationFailure(content);
            Log.e("loadTranslation", content);
        }
    }

    private List<TranslationBean> getTranslationFromContent(String keyword, String content) {
        try {
            List<TranslationBean> list = new ArrayList<>();
            JSONObject lex = new JSONObject(content);
            JSONArray def = lex.getJSONObject("LEX").getJSONArray("C_DEF");
            for (int i = 0; i < def.length(); i++) {
                if (!keyword.contains(" ")) {
                    String pos = def.getJSONObject(i).getString("POS");
                    JSONArray sen = def.getJSONObject(i).getJSONArray("SEN");
                    for (int j = 0; j < sen.length(); j++) {
                        JSONObject item = sen.getJSONObject(j);
                        if (!item.isNull("STS")) {
                            TranslationBean bean = new TranslationBean();
                            bean.setD(item.getString("D"));
                            bean.setPos(pos);
                            bean.setSd(item.getJSONArray("STS").getJSONObject(0).getJSONObject("S").getString("D"));
                            bean.setTd(item.getJSONArray("STS").getJSONObject(0).getJSONObject("T").getString("D"));
                            list.add(bean);
                        }
                    }
                } else {
                    String pos = def.getJSONObject(i).getString("POS");
                    JSONArray sen = def.getJSONObject(i).getJSONArray("SEN");
                    for (int j = 0; j < sen.length(); j++) {
                        JSONObject item = sen.getJSONObject(j);
                        TranslationBean bean = new TranslationBean();
                        bean.setSd(keyword);
                        bean.setTd(item.getString("D"));
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
