package me.lancer.pocket.info.mvp.novel.manager;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.info.mvp.novel.NovelMark;
import me.lancer.pocket.util.AppUtil;
import me.lancer.pocket.util.Constant;
import me.lancer.pocket.util.ScreenUtil;
import me.lancer.pocket.util.SharedPreferencesUtil;

/**
 * Created by HuangFangzhi on 2017/7/13.
 */

public class SettingManager {

    private volatile static SettingManager manager;

    public static SettingManager getInstance() {
        return manager != null ? manager : (manager = new SettingManager());
    }

    /**
     * 保存书籍阅读字体大小
     *
     * @param bookId     需根据bookId对应，避免由于字体大小引起的分页不准确
     * @param fontSizePx
     * @return
     */
    public void saveFontSize(String bookId, int fontSizePx) {
        // 书籍对应
        SharedPreferencesUtil.getInstance().putInt(getFontSizeKey(bookId), fontSizePx);
    }

    /**
     * 保存全局生效的阅读字体大小
     *
     * @param fontSizePx
     */
    public void saveFontSize(int fontSizePx) {
        saveFontSize("", fontSizePx);
    }

    public int getReadFontSize(String bookId) {
        return SharedPreferencesUtil.getInstance().getInt(getFontSizeKey(bookId), ScreenUtil.dpToPxInt(16));
    }

    public int getReadFontSize() {
        return getReadFontSize("");
    }

    private String getFontSizeKey(String bookId) {
        return bookId + "-readFontSize";
    }

    public int getReadBrightness() {
        return SharedPreferencesUtil.getInstance().getInt(getLightnessKey(),
                (int) ScreenUtil.getScreenBrightness(AppUtil.getAppContext()));
    }

    /**
     * 保存阅读界面屏幕亮度
     *
     * @param percent 亮度比例 0~100
     */
    public void saveReadBrightness(int percent) {
        SharedPreferencesUtil.getInstance().putInt(getLightnessKey(), percent);
    }

    private String getLightnessKey() {
        return "readLightness";
    }

    public synchronized void saveReadProgress(String bookId, int currentChapter, int m_mbBufBeginPos, int m_mbBufEndPos) {
        SharedPreferencesUtil.getInstance()
                .putInt(getChapterKey(bookId), currentChapter)
                .putInt(getStartPosKey(bookId), m_mbBufBeginPos)
                .putInt(getEndPosKey(bookId), m_mbBufEndPos);
    }

    /**
     * 获取上次阅读章节及位置
     *
     * @param bookId
     * @return
     */
    public int[] getReadProgress(String bookId) {
        int lastChapter = SharedPreferencesUtil.getInstance().getInt(getChapterKey(bookId), 1);
        int startPos = SharedPreferencesUtil.getInstance().getInt(getStartPosKey(bookId), 0);
        int endPos = SharedPreferencesUtil.getInstance().getInt(getEndPosKey(bookId), 0);

        return new int[]{lastChapter, startPos, endPos};
    }

    public void removeReadProgress(String bookId) {
        SharedPreferencesUtil.getInstance()
                .remove(getChapterKey(bookId))
                .remove(getStartPosKey(bookId))
                .remove(getEndPosKey(bookId));
    }

    private String getChapterKey(String bookId) {
        return bookId + "-chapter";
    }

    private String getStartPosKey(String bookId) {
        return bookId + "-startPos";
    }

    private String getEndPosKey(String bookId) {
        return bookId + "-endPos";
    }


    public boolean addBookMark(String bookId, NovelMark mark) {
        List<NovelMark> marks = SharedPreferencesUtil.getInstance().getObject(getBookMarksKey(bookId), ArrayList.class);
        if (marks != null && marks.size() > 0) {
            for (NovelMark item : marks) {
                if (item.chapter == mark.chapter && item.startPos == mark.startPos) {
                    return false;
                }
            }
        } else {
            marks = new ArrayList<>();
        }
        marks.add(mark);
        SharedPreferencesUtil.getInstance().putObject(getBookMarksKey(bookId), marks);
        return true;
    }

    public List<NovelMark> getBookMarks(String bookId) {
        return SharedPreferencesUtil.getInstance().getObject(getBookMarksKey(bookId), ArrayList.class);
    }

    public void clearBookMarks(String bookId) {
        SharedPreferencesUtil.getInstance().remove(getBookMarksKey(bookId));
    }

    private String getBookMarksKey(String bookId) {
        return bookId + "-marks";
    }

    public void saveReadTheme(int theme) {
        SharedPreferencesUtil.getInstance().putInt("readTheme", theme);
    }

    public int getReadTheme() {
        if (SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false)) {
            return ThemeManager.NIGHT;
        }
        return SharedPreferencesUtil.getInstance().getInt("readTheme", 3);
    }

    /**
     * 是否可以使用音量键翻页
     *
     * @param enable
     */
    public void saveVolumeFlipEnable(boolean enable) {
        SharedPreferencesUtil.getInstance().putBoolean("volumeFlip", enable);
    }

    public boolean isVolumeFlipEnable() {
        return SharedPreferencesUtil.getInstance().getBoolean("volumeFlip", true);
    }

    public void saveAutoBrightness(boolean enable) {
        SharedPreferencesUtil.getInstance().putBoolean("autoBrightness", enable);
    }

    public boolean isAutoBrightness() {
        return SharedPreferencesUtil.getInstance().getBoolean("autoBrightness", false);
    }

    /**
     * 保存用户选择的性别
     *
     * @param sex male female
     */
    public void saveUserChooseSex(String sex) {
        SharedPreferencesUtil.getInstance().putString("userChooseSex", sex);
    }

    /**
     * 获取用户选择性别
     *
     * @return
     */
    public String getUserChooseSex() {
        return SharedPreferencesUtil.getInstance().getString("userChooseSex", Constant.Gender.MALE);
    }

    public boolean isUserChooseSex() {
        return SharedPreferencesUtil.getInstance().exists("userChooseSex");
    }

    public boolean isNoneCover() {
        return SharedPreferencesUtil.getInstance().getBoolean("isNoneCover", false);
    }

    public void saveNoneCover(boolean isNoneCover) {
        SharedPreferencesUtil.getInstance().putBoolean("isNoneCover", isNoneCover);
    }
}
