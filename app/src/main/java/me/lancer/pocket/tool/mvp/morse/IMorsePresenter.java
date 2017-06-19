package me.lancer.pocket.tool.mvp.morse;

import java.util.Map;

/**
 * Created by HuangFangzhi on 2017/6/19.
 */

public interface IMorsePresenter {

    void loadMorseSuccess(Map<String, String> map);

    void loadMorseFailure(String log);
}
