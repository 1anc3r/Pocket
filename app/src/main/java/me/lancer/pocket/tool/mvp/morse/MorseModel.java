package me.lancer.pocket.tool.mvp.morse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HuangFangzhi on 2017/6/19.
 */

public class MorseModel {

    IMorsePresenter presenter;

    public MorseModel(IMorsePresenter presenter) {
        this.presenter = presenter;
    }

    public void loadMorse(String content) {
        Map<String, String> map = new HashMap<>();
        if (!content.contains("获取失败!")) {
            map = getMorseFromContent(content);
            presenter.loadMorseSuccess(map);
        } else {
            presenter.loadMorseFailure(content);
        }
    }

    private Map<String,String> getMorseFromContent(String content) {
        try {
            Map<String, String> map = new HashMap<>();
            JSONArray morseArr = new JSONArray(content);
            for (int i = 0; i < morseArr.length(); i++) {
                JSONObject morseItm = morseArr.getJSONObject(i);
                String ch = morseItm.getString("char");
                String co = morseItm.getString("code");
                map.put(ch, co);
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
