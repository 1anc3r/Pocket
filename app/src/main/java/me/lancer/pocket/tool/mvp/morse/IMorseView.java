package me.lancer.pocket.tool.mvp.morse;

import java.util.Map;

import me.lancer.pocket.tool.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/6/19.
 */

public interface IMorseView extends IBaseView {

    void showMorse(Map<String, String> map);
}
