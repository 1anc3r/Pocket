package me.lancer.pocket.info.mvp.knowledge;

import java.util.List;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface IKnowledgePresenter {

    void loadClassifySuccess(List<KnowledgeBean> list);

    void loadClassifyFailure(String log);

    void loadListSuccess(List<KnowledgeBean> list);

    void loadListFailure(String log);

    void loadNewsSuccess(List<KnowledgeBean> list);

    void loadNewsFailure(String log);

    void loadShowSuccess(KnowledgeBean bean);

    void loadShowFailure(String log);

    void loadSearchSuccess(List<KnowledgeBean> list);

    void loadSearchFailure(String log);
}