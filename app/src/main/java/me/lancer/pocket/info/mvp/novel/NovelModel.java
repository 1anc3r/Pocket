package me.lancer.pocket.info.mvp.novel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.util.ContentGetterSetter;

/**
 * Created by HuangFangzhi on 2017/5/25.
 */

public class NovelModel {

    INovelPresenter presenter;

    ContentGetterSetter contentGetterSetter = new ContentGetterSetter();
    String rankingUrl = "http://api.zhuishushenqi.com/ranking/gender";//不同性别下的榜单http://api.zhuishushenqi.com/ranking/gender
    String catsUrl = "http://api.zhuishushenqi.com/cats/lv2";//指定性别下的分类http://api.zhuishushenqi.com/cats/lv2?gender=male&type=new
    String searchUrl = "http://api.zhuishushenqi.com/book/fuzzy-search?query=";//搜索http://api.zhuishushenqi.com/book/fuzzy-search?query=一念
    String bookUrl = "http://api.zhuishushenqi.com/book/";//图书详情http://api.zhuishushenqi.com/book/573d65ab608bed412452ba69
    String sourceUrl = "http://api.zhuishushenqi.com/toc?view=summary&book=";//书源http://api.zhuishushenqi.com/toc?view=summary&book=573d65ab608bed412452ba69
    String chapterUrl = "http://api.zhuishushenqi.com/toc/";//章节列表http://api.zhuishushenqi.com/toc/57e9357fbf649ec11272de11?view=chapters
    String pagerUrl = "http://chapter2.zhuishushenqi.com/chapter/";//章节内容http://chapter2.zhuishushenqi.com/chapter/http%3a%2f%2fbook.my716.com%2fgetBooks.aspx%3fmethod%3dcontent%26bookId%3d1127281%26chapterFile%3dU_1212539_201701211420571844_4093_2.txt?k=2124b73d7e2e1945&t=1468223717
    String coverUrl = "http://statics.zhuishushenqi.com/";//封皮http://statics.zhuishushenqi.com/agent/http://images.zhulang.com/book_cover/image/18/98/189843.jpg

    public NovelModel(INovelPresenter presenter) {
        this.presenter = presenter;
    }


}
