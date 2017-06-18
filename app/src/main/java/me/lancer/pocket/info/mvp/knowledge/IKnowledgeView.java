package me.lancer.pocket.info.mvp.knowledge;

import java.util.List;

import me.lancer.pocket.info.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public interface IKnowledgeView extends IBaseView {

    void showClassify(List<KnowledgeBean> list);

    void showList(List<KnowledgeBean> list);

    void showNews(List<KnowledgeBean> list);

    void showShow(KnowledgeBean bean);

    void showSearch(List<KnowledgeBean> list);
}
