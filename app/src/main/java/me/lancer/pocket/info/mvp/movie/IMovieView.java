package me.lancer.pocket.info.mvp.movie;

import java.util.List;

import me.lancer.pocket.ui.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface IMovieView extends IBaseView {

    void showReviewer(List<MovieBean> list);

    void showTopMovie(List<MovieBean> list);

    void showReviewerDetail(MovieBean bean);

    void showTopDetail(MovieBean bean);
}
