package me.lancer.pocket.info.mvp.joke;

import java.util.List;

import me.lancer.pocket.info.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface IJokeView extends IBaseView {

    void showText(List<JokeBean> list);

    void showImage(List<JokeBean> list);

    void showVideo(List<JokeBean> list);
}
