package me.lancer.pocket.info.mvp.photo;

import java.util.List;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface IPhotoPresenter {

    void downloadSuccess(String log);

    void downloadFailure(String log);

    void loadPexelsSuccess(List<PhotoBean> list);

    void loadPexelsFailure(String log);

    void loadGankSuccess(List<PhotoBean> list);

    void loadGankFailure(String log);

    void loadHuabanSuccess(List<PhotoBean> list);

    void loadHuabanFailure(String log);
}