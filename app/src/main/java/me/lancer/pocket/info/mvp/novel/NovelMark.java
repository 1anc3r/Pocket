package me.lancer.pocket.info.mvp.novel;

import java.io.Serializable;

/**
 * Created by HuangFangzhi on 2017/5/25.
 */

public class NovelMark implements Serializable {

    public int chapter;

    public String title;

    public int startPos;

    public int endPos;

    public String desc = "";
}
