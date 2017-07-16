package me.lancer.pocket.mainui.collect;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.lancer.pocket.mainui.collect.CollectBean;
import me.lancer.pocket.mainui.collect.ICollectPresenter;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class CollectModel {

    ICollectPresenter presenter;

    SQLiteDatabase db;

    public CollectModel(ICollectPresenter presenter) {
        this.presenter = presenter;
    }

    public void add(CollectBean bean) {
        init();
        ContentValues contentValues = new ContentValues();
//        contentValues.put("day", bean.getDay());
//        contentValues.put("time", bean.getTime());
//        contentValues.put("length", bean.getLength());
//        contentValues.put("name", bean.getName());
//        contentValues.put("location", bean.getLocation());
        long result = db.insert("Collect", null, contentValues);
        if (result == -1) {
            presenter.addCollectResult(result);
        } else {
            presenter.addCollectResult(result);
        }
    }

    public void query() {
        init();
        List<CollectBean> list = new ArrayList<>();
        Cursor cursor = db.query("Collect", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            HashMap<String, Integer> colorMap = new HashMap<>();
            int color = 0;
            while (!cursor.isAfterLast()) {
                CollectBean bean = new CollectBean();
//                bean.setId(cursor.getInt(0));
//                bean.setDay(cursor.getInt(1));
//                bean.setTime(cursor.getInt(2));
//                bean.setLength(cursor.getInt(3));
//                bean.setName(cursor.getString(4));
//                bean.setLocation(cursor.getString(5));
//                if (colorMap.get(bean.getName()) != null) {
//                    bean.setColor(colorMap.get(bean.getName()));
//                } else {
//                    bean.setColor(color);
//                    colorMap.put(bean.getName(), color);
//                    if ((++color) == 7) {
//                        color = 0;
//                    }
//                }
                list.add(bean);
                cursor.moveToNext();
            }
        }
        presenter.queryCollectResult(list);
    }

    public void modify(CollectBean bean) {
        init();
        ContentValues contentValues = new ContentValues();
//        contentValues.put("day", bean.getDay());
//        contentValues.put("time", bean.getTime());
//        contentValues.put("length", bean.getLength());
//        contentValues.put("name", bean.getName());
//        contentValues.put("location", bean.getLocation());
        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(bean.getId())};
        int result = db.update("Collect", contentValues, whereClause, whereArgs);
        if (result == -1) {
            presenter.modifyCollectResult(result);
        } else {
            presenter.modifyCollectResult(result);
        }
    }

    public void delete(CollectBean bean) {
        init();
        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(bean.getId())};
        int result = db.delete("Collect", whereClause, whereArgs);
        if (result == -1) {
            presenter.deleteCollectResult(result);
        } else {
            presenter.deleteCollectResult(result);
        }

    }

    private void init() {
        db = SQLiteDatabase.openOrCreateDatabase("/data/data/me.lancer.pocket/databases/pocket.db", null);
        String createTable = "CREATE TABLE IF NOT EXISTS Collect(_id INTEGER PRIMARY KEY AUTOINCREMENT, day INTEGER, time INTEGER, length INTEGER, name VARCHAR, location VARCHAR)";
        db.execSQL(createTable);
    }
}
