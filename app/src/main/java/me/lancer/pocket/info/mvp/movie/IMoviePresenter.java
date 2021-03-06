package me.lancer.pocket.info.mvp.movie;

import java.util.List;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface IMoviePresenter {

    void loadReviewerSuccess(List<MovieBean> list);

    void loadReviewerFailure(String log);

    void loadTopMovieSuccess(List<MovieBean> list);

    void loadTopMovieFailure(String log);

    void loadReviewerDetailSuccess(MovieBean bean);

    void loadReviewerDetailFailure(String log);

    void loadTopDetailSuccess(MovieBean bean);

    void loadTopDetailFailure(String log);
}