package me.lancer.pocket.tool.mvp.calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HuangFangzhi on 2017/3/13.
 */

public class CalendarModel {

    ICalendarPresenter presenter;

    SQLiteDatabase db;

    public CalendarModel(ICalendarPresenter presenter) {
        this.presenter = presenter;
    }

    public void add(CalendarBean bean) {
        init();
        ContentValues contentValues = new ContentValues();
        contentValues.put("day", bean.getDay());
        contentValues.put("time", bean.getTime());
        contentValues.put("length", bean.getLength());
        contentValues.put("name", bean.getName());
        contentValues.put("location", bean.getLocation());
        long result = db.insert("Calendar", null, contentValues);
        if (result == -1) {
            presenter.addCalendarResult(result);
        } else {
            presenter.addCalendarResult(result);
        }
    }

    public void query() {
        init();
        List<CalendarBean> list = new ArrayList<>();
        Cursor cursor = db.query("Calendar", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            HashMap<String, Integer> colorMap = new HashMap<>();
            int color = 0;
            while (!cursor.isAfterLast()) {
                CalendarBean bean = new CalendarBean();
                bean.setId(cursor.getInt(0));
                bean.setDay(cursor.getInt(1));
                bean.setTime(cursor.getInt(2));
                bean.setLength(cursor.getInt(3));
                bean.setName(cursor.getString(4));
                bean.setLocation(cursor.getString(5));
                if (colorMap.get(bean.getName()) != null) {
                    bean.setColor(colorMap.get(bean.getName()));
                } else {
                    bean.setColor(color);
                    colorMap.put(bean.getName(), color);
                    if ((++color) == 7) {
                        color = 0;
                    }
                }
                list.add(bean);
                cursor.moveToNext();
            }
        }
        presenter.queryCalendarResult(list);
    }

    public void modify(CalendarBean bean) {
        init();
        ContentValues contentValues = new ContentValues();
        contentValues.put("day", bean.getDay());
        contentValues.put("time", bean.getTime());
        contentValues.put("length", bean.getLength());
        contentValues.put("name", bean.getName());
        contentValues.put("location", bean.getLocation());
        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(bean.getId())};
        int result = db.update("Calendar", contentValues, whereClause, whereArgs);
        if (result == -1) {
            presenter.modifyCalendarResult(result);
        } else {
            presenter.modifyCalendarResult(result);
        }
    }

    public void delete(CalendarBean bean) {
        init();
        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(bean.getId())};
        int result = db.delete("Calendar", whereClause, whereArgs);
        if (result == -1) {
            presenter.deleteCalendarResult(result);
        } else {
            presenter.deleteCalendarResult(result);
        }

    }

    private void init() {
        db = SQLiteDatabase.openOrCreateDatabase("/data/data/me.lancer.pocket/databases/calender.db", null);
        String createTable = "CREATE TABLE IF NOT EXISTS Calendar(_id INTEGER PRIMARY KEY AUTOINCREMENT, day INTEGER, time INTEGER, length INTEGER, name VARCHAR, location VARCHAR)";
        db.execSQL(createTable);
    }
}
