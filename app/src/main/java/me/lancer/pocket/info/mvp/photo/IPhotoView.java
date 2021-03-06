package me.lancer.pocket.info.mvp.photo;

import java.util.List;

import me.lancer.pocket.ui.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface IPhotoView extends IBaseView {

    void showPexels(List<PhotoBean> list);

    void showGank(List<PhotoBean> list);

    void showHuaban(List<PhotoBean> list);
}
