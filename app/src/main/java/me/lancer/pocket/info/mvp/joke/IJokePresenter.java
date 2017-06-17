package me.lancer.pocket.info.mvp.joke;

import java.util.List;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface IJokePresenter {

    void loadTextSuccess(List<JokeBean> list);

    void loadTextFailure(String log);

    void loadImageSuccess(List<JokeBean> list);

    void loadImageFailure(String log);

    void loadVideoSuccess(List<JokeBean> list);

    void loadVideoFailure(String log);
}