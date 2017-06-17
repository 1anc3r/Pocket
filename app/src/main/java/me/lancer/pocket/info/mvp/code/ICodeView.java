package me.lancer.pocket.info.mvp.code;

import java.util.List;

import me.lancer.pocket.info.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface ICodeView extends IBaseView {

    void showUsers(List<CodeBean> list);

    void showOrganizations(List<CodeBean> list);

    void showRepositories(List<CodeBean> list);

    void showTrending(List<CodeBean> list);

    void showSearching(List<CodeBean> list);

    void showDetail(CodeBean bean);
}
