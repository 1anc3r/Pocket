package me.lancer.pocket.info.mvp.game;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.lancer.pocket.util.ContentGetterSetter;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class GameModel {

    IGamePresenter presenter;

    ContentGetterSetter contentGetterSetter = new ContentGetterSetter();
    String featuredUrl = "http://store.steampowered.com/api/featured/";
    String categoriesUrl = "http://store.steampowered.com/api/featuredcategories/";
    String appdetailsUrl = "http://store.steampowered.com/api/appdetails?appids=";

    public GameModel(IGamePresenter presenter) {
        this.presenter = presenter;
    }

    public void loadFeatured() {
        String content = contentGetterSetter.getContentFromHtml("Game.loadFeatured", featuredUrl);
        List<GameBean> list;
        if (!content.contains("获取失败!")) {
            list = getFeaturedFromContent(content);
            presenter.loadFeaturedSuccess(list);
        } else {
            presenter.loadFeaturedFailure(content);
        }
    }

    public void loadCategories(String keyword) {
        String path = Environment.getExternalStorageDirectory().toString();
        String content;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date today = new Date(new Date().getTime());
        Date yestoday = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String arg1 = format.format(today);
        String arg2 = format.format(yestoday);
        List<GameBean> list;
        if (!(content = contentGetterSetter.getContentFromFile(path, arg1)).contains("失败!")) {
            list = getCategoriesFromContent(keyword, content);
            presenter.loadCategoriesSuccess(list);
        } else if (!(content = contentGetterSetter.getContentFromHtml("Game.loadCategories", categoriesUrl)).contains("获取失败!")) {
            contentGetterSetter.setContentToFile(path, arg1, arg2, content);
            list = getCategoriesFromContent(keyword, content);
            presenter.loadCategoriesSuccess(list);
        } else {
            presenter.loadCategoriesFailure(content);
        }
    }

    public void loadDetail(int id) {
        if (id == 54029) {
            id = 730;
        }
        String content = contentGetterSetter.getContentFromHtml("Game.loadDetail", appdetailsUrl + id);
        GameBean bean;
        if (!content.contains("获取失败!")) {
            bean = getDetailFromContent(id, content);
            if (bean != null) {
                presenter.loadDetailSuccess(bean);
            } else {
                presenter.loadDetailFailure("获取失败!坏数据");
            }
        } else {
            presenter.loadDetailFailure(content);
        }
    }

    public List<GameBean> getFeaturedFromContent(String content) {
        try {
            List<GameBean> list = new ArrayList<>();
            JSONObject all = new JSONObject(content);
            JSONArray largeCapsules = all.getJSONArray("large_capsules");
            for (int i = 0; i < largeCapsules.length(); i++) {
                JSONObject jbItem = (JSONObject) largeCapsules.get(i);
                GameBean bean = new GameBean();
                bean.setId(jbItem.getInt("id"));
                bean.setIntType(0);
                bean.setName(jbItem.getString("name"));
                bean.setDiscounted(jbItem.getBoolean("discounted"));
                bean.setDiscountPercent(jbItem.getInt("discount_percent"));
                if (!jbItem.get("original_price").toString().equals("null")) {
                    bean.setOriginalPrice(jbItem.getInt("original_price") / 100);
                }
                if (!jbItem.get("final_price").toString().equals("null")) {
                    bean.setFinalPrice(jbItem.getInt("final_price") / 100);
                }
                bean.setWindowsAvailable(jbItem.getBoolean("windows_available"));
                bean.setMacAvailable(jbItem.getBoolean("mac_available"));
                bean.setLinuxAvailable(jbItem.getBoolean("linux_available"));
                bean.setCurrency(jbItem.getString("currency"));
                bean.setHeaderImage(jbItem.getString("header_image"));
                list.add(bean);
            }
            JSONArray featuredWin = all.getJSONArray("featured_win");
            for (int i = 0; i < featuredWin.length(); i++) {
                JSONObject jbItem = (JSONObject) featuredWin.get(i);
                GameBean bean = new GameBean();
                bean.setId(jbItem.getInt("id"));
                bean.setIntType(0);
                bean.setName(jbItem.getString("name"));
                bean.setDiscounted(jbItem.getBoolean("discounted"));
                bean.setDiscountPercent(jbItem.getInt("discount_percent"));
                if (!jbItem.get("original_price").toString().equals("null")) {
                    bean.setOriginalPrice(jbItem.getInt("original_price") / 100);
                }
                if (!jbItem.get("final_price").toString().equals("null")) {
                    bean.setFinalPrice(jbItem.getInt("final_price") / 100);
                }
                bean.setWindowsAvailable(jbItem.getBoolean("windows_available"));
                bean.setMacAvailable(jbItem.getBoolean("mac_available"));
                bean.setLinuxAvailable(jbItem.getBoolean("linux_available"));
                bean.setCurrency(jbItem.getString("currency"));
                bean.setHeaderImage(jbItem.getString("header_image"));
                list.add(bean);
            }
            JSONArray featuredMac = all.getJSONArray("featured_mac");
            for (int i = 0; i < featuredMac.length(); i++) {
                JSONObject jbItem = (JSONObject) featuredMac.get(i);
                GameBean bean = new GameBean();
                bean.setId(jbItem.getInt("id"));
                bean.setIntType(0);
                bean.setName(jbItem.getString("name"));
                bean.setDiscounted(jbItem.getBoolean("discounted"));
                bean.setDiscountPercent(jbItem.getInt("discount_percent"));
                if (!jbItem.get("original_price").toString().equals("null")) {
                    bean.setOriginalPrice(jbItem.getInt("original_price") / 100);
                }
                if (!jbItem.get("final_price").toString().equals("null")) {
                    bean.setFinalPrice(jbItem.getInt("final_price") / 100);
                }
                bean.setWindowsAvailable(jbItem.getBoolean("windows_available"));
                bean.setMacAvailable(jbItem.getBoolean("mac_available"));
                bean.setLinuxAvailable(jbItem.getBoolean("linux_available"));
                bean.setCurrency(jbItem.getString("currency"));
                bean.setHeaderImage(jbItem.getString("header_image"));
                list.add(bean);
            }
            JSONArray featuredLinux = all.getJSONArray("featured_linux");
            for (int i = 0; i < featuredLinux.length(); i++) {
                JSONObject jbItem = (JSONObject) featuredLinux.get(i);
                GameBean bean = new GameBean();
                bean.setId(jbItem.getInt("id"));
                bean.setIntType(0);
                bean.setName(jbItem.getString("name"));
                bean.setDiscounted(jbItem.getBoolean("discounted"));
                bean.setDiscountPercent(jbItem.getInt("discount_percent"));
                if (!jbItem.get("original_price").toString().equals("null")) {
                    bean.setOriginalPrice(jbItem.getInt("original_price") / 100);
                }
                if (!jbItem.get("final_price").toString().equals("null")) {
                    bean.setFinalPrice(jbItem.getInt("final_price") / 100);
                }
                bean.setWindowsAvailable(jbItem.getBoolean("windows_available"));
                bean.setMacAvailable(jbItem.getBoolean("mac_available"));
                bean.setLinuxAvailable(jbItem.getBoolean("linux_available"));
                bean.setCurrency(jbItem.getString("currency"));
                bean.setHeaderImage(jbItem.getString("header_image"));
                list.add(bean);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<GameBean> getCategoriesFromContent(String keyword, String content) {
        try {
            List<GameBean> list = new ArrayList<>();
            JSONObject all = new JSONObject(content);
            JSONObject object = all.getJSONObject(keyword);
            JSONArray array = object.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jbItem = (JSONObject) array.get(i);
                GameBean bean = new GameBean();
                bean.setId(jbItem.getInt("id"));
                bean.setIntType(0);
                bean.setName(jbItem.getString("name"));
                bean.setDiscounted(jbItem.getBoolean("discounted"));
                bean.setDiscountPercent(jbItem.getInt("discount_percent"));
                if (!jbItem.get("original_price").toString().equals("null")) {
                    bean.setOriginalPrice(jbItem.getInt("original_price") / 100);
                }
                if (!jbItem.get("final_price").toString().equals("null")) {
                    bean.setFinalPrice(jbItem.getInt("final_price") / 100);
                }
                bean.setWindowsAvailable(jbItem.getBoolean("windows_available"));
                bean.setMacAvailable(jbItem.getBoolean("mac_available"));
                bean.setLinuxAvailable(jbItem.getBoolean("linux_available"));
                bean.setCurrency(jbItem.getString("currency"));
                bean.setHeaderImage(jbItem.getString("header_image"));
                list.add(bean);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GameBean getDetailFromContent(int id, String content) {
        try {
            GameBean bean = new GameBean();
            JSONObject all = new JSONObject(content);
            all = all.getJSONObject("" + id);
            if (all.getBoolean("success")) {
                JSONObject data = all.getJSONObject("data");
                bean.setIntType(0);
                bean.setName(data.getString("name"));
                bean.setDescription(data.getString("detailed_description"));
                bean.setSupportedLanguages(data.getString("supported_languages"));
                String requirements = "";
                JSONObject pc = data.getJSONObject("pc_requirements");
                if (pc.has("minimum")) {
                    requirements += pc.getString("minimum") + "<br>";
                }
                if (pc.has("recommended")) {
                    requirements += pc.getString("recommended") + "<br>";
                }
                bean.setRequirements(requirements);
                bean.setDevelopers((String) data.getJSONArray("developers").get(0));
                bean.setPublishers((String) data.getJSONArray("publishers").get(0));
                if (data.has("price_overview")) {
                    JSONObject priceOverview = data.getJSONObject("price_overview");
                    bean.setCurrency(priceOverview.getString("currency"));
                    if (!priceOverview.get("initial").toString().equals("null")) {
                        bean.setOriginalPrice(priceOverview.getInt("initial") / 100);
                    }
                    if (!priceOverview.get("final").toString().equals("null")) {
                        bean.setFinalPrice(priceOverview.getInt("final") / 100);
                    }
                    bean.setDiscountPercent(priceOverview.getInt("discount_percent"));
                }
                JSONArray jshots = data.getJSONArray("screenshots");
                List<String> lshots = new ArrayList<>();
                for (int j = 0; j < jshots.length(); j++) {
                    lshots.add(((JSONObject) jshots.get(j)).getString("path_thumbnail"));
                }
                bean.setScreenshots(lshots);
                return bean;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
