package me.lancer.pocket.info.mvp.video;

import java.util.List;

import me.lancer.pocket.ui.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface IVideoView extends IBaseView {

    void showTheme(List<VideoBean> list);

    void showDetail(VideoBean bean);
}
