package me.lancer.pocket.tool.mvp.calendar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.polaric.colorful.Colorful;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.lancer.pocket.tool.mvp.calendar.bean.CalendarBean;
import me.lancer.pocket.util.DensityUtil;

import static me.lancer.pocket.R.*;
import static me.lancer.pocket.R.style.CalendarView;

/**
 * Created by HuangFangzhi on 2016/12/16.
 */

public class CalendarView extends RelativeLayout {

    private FrameLayout flScheduleContent;
    private List<View> arrCacheView = new ArrayList<>();
    private TextView tvMonthNum, tvMonthStr;

    private static final int[] Schedule_BG = {drawable.schedule_content_teal, drawable.schedule_content_blue,
            drawable.schedule_content_red, drawable.schedule_content_pink, drawable.schedule_content_yellow,
            drawable.schedule_content_green, drawable.schedule_content_orage};

    private static final int TV_MONTH_ID = 11;
    private static final int TV_WEEK_ID = 26;

    private List<? extends CalendarBean> arrSchedule;
    private String[] CN_DAYS = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    private int squareHeight;
    private int squareWidth;
    private int todayDate;
    private String[] arrDate;
    private int weight;
    private int totalTime = 12;
    private int totalDay = 7;
    private String preMonth;

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, styleable.Schedule, defStyleAttr, 0);
        totalDay = ta.getInt(styleable.Schedule_totalDay, 7);
        totalTime = ta.getInt(styleable.Schedule_totalTime, 12);
        ta.recycle();
        init(context);
        draw();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context) {
        this(context, null);
    }

    private OnScheduleItemClickListener onScheduleItemClickListener;

    public void setOnScheduleItemClickListener(OnScheduleItemClickListener onScheduleItemClickListener) {
        this.onScheduleItemClickListener = onScheduleItemClickListener;
    }

    public interface OnScheduleItemClickListener {
        void onScheduleItemClick(TextView tv, int time, int day, String des);
    }

    private void initSize() {
        int screenWidth = getScreenWidth();
        int screenHeight = getScreenHeight();
        squareHeight = screenHeight / 10;
        squareWidth = screenWidth / 8;
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private String[] getArrDate() {
        Calendar todayDateCal = Calendar.getInstance();
        todayDateCal.setTimeInMillis(System.currentTimeMillis());
        String[] temp = new String[totalDay];
        int b = todayDateCal.get(Calendar.DAY_OF_WEEK) - 2;
        if (b != 6) {
            todayDateCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        } else {
            todayDateCal.add(Calendar.WEEK_OF_MONTH, -1);
            todayDateCal.set(Calendar.DAY_OF_WEEK, 2);
        }
        int ds = 0;
        for (int i = 1; i < totalDay; i++) {
            if (i == 1) {
                ds = todayDateCal.get(Calendar.DAY_OF_MONTH);
                temp[i - 1] = todayDateCal.get(Calendar.DAY_OF_MONTH) + "";
                preMonth = (todayDateCal.get(Calendar.MONTH) + 1) + "";
            }
            todayDateCal.add(Calendar.DATE, 1);
            if (todayDateCal.get(Calendar.DAY_OF_MONTH) < ds) {
                temp[i] = (todayDateCal.get(Calendar.MONTH) + 1) + "月";
                ds = todayDateCal.get(Calendar.DAY_OF_MONTH);
            } else {
                temp[i] = todayDateCal.get(Calendar.DAY_OF_MONTH) + "";
            }
        }
        return temp;
    }

    public String[] setArrDate(Date date) {
        Calendar todayDateCal = Calendar.getInstance();
        todayDateCal.setTimeInMillis(date.getTime());
        String[] temp = new String[totalDay];
        int b = todayDateCal.get(Calendar.DAY_OF_WEEK) - 2;
        if (b != 6) {
            todayDateCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        } else {
            todayDateCal.add(Calendar.WEEK_OF_MONTH, -1);
            todayDateCal.set(Calendar.DAY_OF_WEEK, 2);
        }
        int ds = 0;
        for (int i = 1; i < totalDay; i++) {
            if (i == 1) {
                ds = todayDateCal.get(Calendar.DAY_OF_MONTH);
                temp[i - 1] = todayDateCal.get(Calendar.DAY_OF_MONTH) + "";
                preMonth = (todayDateCal.get(Calendar.MONTH) + 1) + "";
            }
            todayDateCal.add(Calendar.DATE, 1);
            if (todayDateCal.get(Calendar.DAY_OF_MONTH) < ds) {
                temp[i] = (todayDateCal.get(Calendar.MONTH) + 1) + "月";
                ds = todayDateCal.get(Calendar.DAY_OF_MONTH);
            } else {
                temp[i] = todayDateCal.get(Calendar.DAY_OF_MONTH) + "";
            }
        }
        return temp;
    }

    private void init(Context context) {
        Calendar todayDateCal = Calendar.getInstance();
        todayDateCal.setTimeInMillis(System.currentTimeMillis());
        todayDate = todayDateCal.get(Calendar.DAY_OF_WEEK) - 2;
        weight = DensityUtil.dip2px(context, 2);
        arrDate = getArrDate();
    }

    private void init(Context context, Date date) {
        Calendar todayDateCal = Calendar.getInstance();
        todayDateCal.setTimeInMillis(date.getTime());
        todayDate = todayDateCal.get(Calendar.DAY_OF_WEEK) - 2;
        weight = DensityUtil.dip2px(context, 2);
        arrDate = setArrDate(date);
    }

    private void draw() {
        initSize();
        drawTopView();
        drawBottomView();
    }

    private void drawTopView() {
        drawMonthView();
        drawWeekView();
    }

    private LinearLayout llMonth;

    @SuppressWarnings("ResourceType")
    private void drawMonthView() {
        LayoutParams rlp;
        LinearLayout.LayoutParams llp;
        llMonth = new LinearLayout(getContext(), null, style.CalendarView);
        llMonth.setOrientation(LinearLayout.VERTICAL);
        llMonth.setId(TV_MONTH_ID);
        rlp = new LayoutParams(squareWidth,
                squareHeight);
        llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llMonth.setLayoutParams(rlp);
        tvMonthNum = new TextView(getContext());
        tvMonthNum.setText(preMonth);
        tvMonthNum.setLayoutParams(llp);
        tvMonthNum.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        tvMonthNum.setPadding(0, weight * 2, 0, -weight);
        tvMonthNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        tvMonthNum.setTextColor(0xFF999999);
        llMonth.addView(tvMonthNum);
        tvMonthStr = new TextView(getContext());
        tvMonthStr.setLayoutParams(llp);
        tvMonthStr.setText("月");
        tvMonthStr.setGravity(Gravity.CENTER | Gravity.TOP);
        tvMonthStr.setTextColor(0xFF999999);
        tvMonthStr.setPadding(0, -weight, 0, weight * 2);
        tvMonthStr.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        llMonth.addView(tvMonthStr);
        addView(llMonth);
    }

    private void drawWeekView() {
        LinearLayout ll;
        LayoutParams rlp;
        LinearLayout.LayoutParams llp;
        TextView tvDate;
        TextView tvWeek;
        for (int i = 0; i < totalDay; i++) {
            ll = new LinearLayout(getContext(), null, style.CalendarView);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setId(TV_WEEK_ID + i);
            rlp = new LayoutParams(squareWidth,
                    squareHeight);
            if (i == 0) {
                rlp.addRule(RelativeLayout.RIGHT_OF, llMonth.getId());
            } else {
                rlp.addRule(RelativeLayout.RIGHT_OF, TV_WEEK_ID + i - 1);
            }
            ll.setLayoutParams(rlp);
            llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            tvDate = new TextView(getContext());
            if (arrDate[i].contains("月")) {
                tvDate.setText(arrDate[i].split("月")[0]);
            } else {
                tvDate.setText(arrDate[i]);
            }
            tvDate.setLayoutParams(llp);
            tvDate.setGravity(Gravity.CENTER | Gravity.BOTTOM);
            tvDate.setPadding(0, weight * 2, 0, -weight);
            tvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            tvDate.setTextColor(0xFF999999);
            ll.addView(tvDate);
            llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            tvWeek = new TextView(getContext());
            tvWeek.setLayoutParams(llp);
            if (arrDate[i].contains("月")) {
                tvWeek.setText("月");
            } else {
                tvWeek.setText(CN_DAYS[i]);
            }
            tvWeek.setGravity(Gravity.CENTER | Gravity.TOP);
            tvWeek.setTextColor(0xFF999999);
            tvWeek.setPadding(0, -weight, 0, weight * 2);
            tvWeek.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            if (todayDate == i) {
                ll.setBackgroundColor(getResources().getColor(Colorful.getThemeDelegate().getPrimaryColor().getColorRes()));
                tvWeek.setTextColor(Color.WHITE);
                tvDate.setTextColor(Color.WHITE);
            }
            ll.addView(tvWeek);
            addView(ll);
        }
    }

    private void drawBottomView() {
        ScrollView sv = new ScrollView(getContext());
        sv.setOverScrollMode(OVER_SCROLL_NEVER);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, llMonth.getId());
        sv.setLayoutParams(lp);
        sv.setVerticalScrollBarEnabled(false);

        LinearLayout llBottom = new LinearLayout(getContext());
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llBottom.setLayoutParams(vlp);

        LinearLayout llLeft = new LinearLayout(getContext());
        LinearLayout.LayoutParams llp1 = new LinearLayout.LayoutParams(squareWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        llLeft.setLayoutParams(llp1);
        llLeft.setOrientation(LinearLayout.VERTICAL);
        drawLeftView(llLeft);
        llBottom.addView(llLeft);

        flScheduleContent = new FrameLayout(getContext());
        LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        flScheduleContent.setLayoutParams(llp2);
        drawScheduleFrame();
        llBottom.addView(flScheduleContent);
        sv.addView(llBottom);

        addView(sv);
    }

    private void drawLeftView(LinearLayout llLeft) {
        LinearLayout ll;
        LayoutParams lp;
        LinearLayout.LayoutParams llp;
        TextView tvDate;
        for (int i = 0; i < totalTime; i++) {
            ll = new LinearLayout(getContext(), null, style.CalendarView);
            ll.setOrientation(LinearLayout.VERTICAL);
            lp = new LayoutParams(squareWidth,
                    squareHeight);
            if (i == 0) {
                lp.addRule(RelativeLayout.RIGHT_OF, llMonth.getId());
            } else {
                lp.addRule(RelativeLayout.RIGHT_OF, TV_WEEK_ID + i - 1);
            }
            ll.setLayoutParams(lp);
            llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            tvDate = new TextView(getContext());
            tvDate.setText((i + 1) + "");
            tvDate.setLayoutParams(llp);
            tvDate.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            tvDate.setPadding(0, 0, 0, 0);
            tvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tvDate.setTextColor(0xFF999999);
            ll.addView(tvDate);
            llLeft.addView(ll);
        }
    }

    private void drawScheduleFrame() {
        for (int i = 0; i < totalDay * totalTime; i++) {
            final int row = i / totalDay;
            final int col = i % totalDay;
            //noinspection ResourceType
            FrameLayout fl = new FrameLayout(getContext(), null, style.CalendarView);
            FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(squareWidth,
                    squareHeight);
            flp.setMargins(col * squareWidth, row * squareHeight, 0, 0);
            fl.setLayoutParams(flp);
            flScheduleContent.addView(fl);
        }
    }

    public void updateSchedule(List<? extends CalendarBean> arrSchedule) {
        this.arrSchedule = arrSchedule;
        updateSchedule();
    }

    private void updateSchedule() {
        clearViewsIfNeeded();
        FrameLayout fl;
        FrameLayout.LayoutParams flp;
        TextView tvSchedule;
        for (final CalendarBean c : arrSchedule) {
            final int time = c.getTime();
            final int day = c.getDay();
            fl = new FrameLayout(getContext());
            flp = new FrameLayout.LayoutParams(squareWidth, squareHeight * c.getLength());
            flp.setMargins((day - 1) * squareWidth, (time - 1) * squareHeight, 0, 0);
            fl.setLayoutParams(flp);
            fl.setPadding(weight, weight, weight, weight);
            tvSchedule = new TextView(getContext());
            flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            tvSchedule.setText(c.getName() + "\n" + c.getLocation());
            tvSchedule.setGravity(Gravity.CENTER);
            tvSchedule.setTextColor(Color.WHITE);
            tvSchedule.setPadding(weight, 0, weight, 0);
            tvSchedule.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tvSchedule.setEllipsize(TextUtils.TruncateAt.END);
            tvSchedule.setLines(7);
            tvSchedule.setBackgroundResource(Schedule_BG[c.getColor()]);
            tvSchedule.setLayoutParams(flp);
            tvSchedule.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onScheduleItemClickListener != null) {
                        onScheduleItemClickListener.onScheduleItemClick((TextView) v, time, day, c.getName());
                    }
                }
            });
            fl.addView(tvSchedule);
            arrCacheView.add(fl);
            flScheduleContent.addView(fl);
        }
    }

    private void clearViewsIfNeeded() {
        if (arrCacheView == null || arrCacheView.isEmpty()) {
            return;
        }
        for (int i = arrCacheView.size() - 1; i >= 0; i--) {
            flScheduleContent.removeView(arrCacheView.get(i));
            arrCacheView.remove(i);
        }
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
        refreshCurrentLayout();
    }

    public void setTotalDay(int totalDay) {
        this.totalDay = totalDay;
        refreshCurrentLayout();
    }

    private void refreshCurrentLayout() {
        removeAllViews();
        init(getContext());
        draw();
        updateSchedule();
    }

    public void refreshCurrentLayout(Date date) {
        removeAllViews();
        init(getContext(), date);
        draw();
        updateSchedule();
    }
}
