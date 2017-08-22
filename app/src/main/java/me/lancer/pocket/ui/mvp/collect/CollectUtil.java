package me.lancer.pocket.ui.mvp.collect;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public final class CollectUtil {

    static SQLiteDatabase db;

    public static long add(CollectBean bean) {
        init();
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", bean.getType());
        contentValues.put("cate", bean.getCate());
        contentValues.put("model", bean.getModel());
        contentValues.put("title", bean.getTitle());
        contentValues.put("cover", bean.getCover());
        contentValues.put("link", bean.getLink());
        return db.insert("Collect", null, contentValues);
    }

    public static List<CollectBean> query() {
        init();
        List<CollectBean> list = new ArrayList<>();
        Cursor cursor = db.query("Collect", null, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                CollectBean bean = new CollectBean();
                bean.setId(cursor.getInt(0));
                bean.setType(cursor.getInt(1));
                bean.setCate(cursor.getInt(2));
                bean.setModel(cursor.getInt(3));
                bean.setTitle(cursor.getString(4));
                bean.setCover(cursor.getString(5));
                bean.setLink(cursor.getString(6));
                list.add(bean);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    public static List<CollectBean> query(String title, String link) {
        init();
        List<CollectBean> list = new ArrayList<>();
        Cursor cursor = db.query("Collect", null, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                CollectBean bean = new CollectBean();
                bean.setId(cursor.getInt(0));
                bean.setType(cursor.getInt(1));
                bean.setCate(cursor.getInt(2));
                bean.setModel(cursor.getInt(3));
                bean.setTitle(cursor.getString(4));
                bean.setCover(cursor.getString(5));
                bean.setLink(cursor.getString(6));
                if (bean.getTitle().equals(title) && bean.getLink().equals(link)) {
                    list.add(bean);
                    cursor.close();
                    return list;
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    public static int modify(CollectBean bean) {
        init();
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", bean.getType());
        contentValues.put("cate", bean.getCate());
        contentValues.put("model", bean.getModel());
        contentValues.put("title", bean.getTitle());
        contentValues.put("cover", bean.getCover());
        contentValues.put("link", bean.getLink());
        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(bean.getId())};
        return db.update("Collect", contentValues, whereClause, whereArgs);
    }

    public static int delete(CollectBean bean) {
        init();
        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(bean.getId())};
        return db.delete("Collect", whereClause, whereArgs);
    }

    private static void init() {
        db = SQLiteDatabase.openOrCreateDatabase("/data/data/me.lancer.pocket/databases/collect.db", null);
        String createTable = "CREATE TABLE IF NOT EXISTS Collect(_id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER, cate INTEGER, model INTEGER, title VARCHAR, cover VARCHAR, link VARCHAR)";
        db.execSQL(createTable);
    }
}
