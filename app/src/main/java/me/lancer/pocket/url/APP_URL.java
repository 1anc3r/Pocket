package me.lancer.pocket.url;

/**
 * Created by HuangFangzhi on 2017/7/17.
 */

public final class APP_URL {
    public static final String COOKIE = "coolapk_did=d41d8cd98f00b204e9800998ecf8427e";
    public static final String HOME_URL = "http://api.coolapk.com/market/v2/api.php?method=getHomepageApkList&slm=1&p=%d&apikey=5b90704e1db879af6f5ee08ec1e8f2a5";
    public static final String DETAIL_URL = "http://api.coolapk.com/market/v2/api.php?method=getApkField&slm=1&includeMeta=0&id=%s&apikey=5b90704e1db879af6f5ee08ec1e8f2a5";
    public static final String SEARCH_URL = "http://api.coolapk.com/market/v2/api.php?method=getSearchApkList&slm=1&q=%s&p=%d&apikey=5b90704e1db879af6f5ee08ec1e8f2a5";
    public static final String UPGRADE_URL = "http://api.coolapk.com/market/v2/api.php?method=getUpgradeVersions&apikey=5b90704e1db879af6f5ee08ec1e8f2a5sdk=24&pkgs=";
    public static final String DOWN_URL = "http://dl-cdn.coolapkmarket.com/down";
}
