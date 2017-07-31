package me.lancer.pocket.info.mvp.book;

import java.util.List;

import me.lancer.pocket.ui.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface IBookView extends IBaseView {

    void showReviewer(List<BookBean> list);

    void showTopBook(List<BookBean> list);

    void showReviewerDetail(BookBean bean);

    void showTopDetail(BookBean bean);
}
