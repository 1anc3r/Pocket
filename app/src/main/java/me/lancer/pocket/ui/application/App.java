package me.lancer.pocket.ui.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

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

    public static Typeface TypeFace;
    private boolean isNight, isScroll, isColorful;
    private int colNumber;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.spf_user), Context.MODE_PRIVATE);
        isNight = sharedPreferences.getBoolean(Params.ISNIGHT, false);
        isScroll = sharedPreferences.getBoolean(Params.ISSCROLL, false);
        colNumber = sharedPreferences.getInt(Params.COLNUMBER, 3);
        isColorful = sharedPreferences.getBoolean(Params.ISCOLORFUL, false);
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

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }

    public int getColNumber() {
        return colNumber;
    }

    public void setColNumber(int colNumber) {
        this.colNumber = colNumber;
    }

    public boolean isScroll() {
        return isScroll;
    }

    public void setScroll(boolean scorll) {
        isScroll = scorll;
    }

    public boolean isColorful() {
        return isColorful;
    }

    public void setColorful(boolean colorful) {
        isColorful = colorful;
    }
}
