package me.lancer.pocket.ui.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ClearCacheRequest;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.Volley;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;

import org.litepal.LitePalApplication;
import org.polaric.colorful.Colorful;

import java.io.File;
import java.lang.reflect.Field;

import me.lancer.pocket.R;

/**
 * Created by HuangFangzhi on 2016/12/15.
 */

public class App extends LitePalApplication {

    private RequestQueue mRequestQueue;

    public static Typeface TypeFace;
    private boolean isPicture, isFirst, isNight;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.spf_user), Context.MODE_PRIVATE);
        isNight = sharedPreferences.getBoolean(Params.ISNIGHT, false);
//        if (isNight) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }
        if (isNight) {
            Colorful.defaults()
                    .primaryColor(Colorful.ThemeColor.DEEP_ORANGE)
                    .accentColor(Colorful.ThemeColor.DEEP_ORANGE)
                    .translucent(false)
                    .dark(isNight);
        } else {
            Colorful.defaults()
                    .primaryColor(Colorful.ThemeColor.GREEN)
                    .accentColor(Colorful.ThemeColor.GREEN)
                    .translucent(false)
                    .dark(isNight);
        }
        Colorful.init(this);
        TypeFace = Typeface.createFromAsset(getAssets(), "fonts/MaterialIcons_Regular.ttf");
        try {
            Field field = Typeface.class.getDeclaredField("SERIF");
            field.setAccessible(true);
            field.set(null, TypeFace);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        new Instabug.Builder(this, "11ada4a3f59b870a986faa4816a3e513")
                .setInvocationEvent(InstabugInvocationEvent.NONE)
                .setEmailFieldVisibility(false)
                .build();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        File cacheDir = new File(this.getCacheDir(), "volley");
        DiskBasedCache cache = new DiskBasedCache(cacheDir);
        mRequestQueue.start();
        mRequestQueue.add(new ClearCacheRequest(cache, null));
        return mRequestQueue;
    }

    public boolean isPicture() {
        return isPicture;
    }

    public void setPicture(boolean picture) {
        isPicture = picture;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }
}
