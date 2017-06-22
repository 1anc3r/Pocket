package me.lancer.pocket.tool.mvp.calendar.activity;

import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.base.activity.BaseActivity;
import me.lancer.pocket.tool.mvp.calendar.bean.CalendarBean;
import me.lancer.pocket.tool.mvp.calendar.view.CalendarView;

public class CalendarActivity extends BaseActivity {

    private AppBarLayout mAppBarLayout;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("M", /*Locale.getDefault()*/Locale.ENGLISH);
    private CompactCalendarView mCompactCalendarView;
    private CalendarView mCalendarView;

    private List<CalendarBean> mList = new ArrayList<>();
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();
        initData();
    }

    private void initData() {
        CalendarBean bean1 = new CalendarBean();
        bean1.setColor(1);
        bean1.setDay(1);
        bean1.setTime(1);
        bean1.setLength(2);
        bean1.setLocation("FZ319");
        bean1.setName("移动应用开发");
        mList.add(bean1);
        CalendarBean bean2 = new CalendarBean();
        bean2.setColor(2);
        bean2.setDay(1);
        bean2.setTime(3);
        bean2.setLength(2);
        bean2.setLocation("FZ319");
        bean2.setName("计算机组成原理");
        mList.add(bean2);
        CalendarBean bean3 = new CalendarBean();
        bean3.setColor(3);
        bean3.setDay(1);
        bean3.setTime(7);
        bean3.setLength(2);
        bean3.setLocation("FZ319");
        bean3.setName("网络编程技术");
        mList.add(bean3);
        CalendarBean bean4 = new CalendarBean();
        bean4.setColor(4);
        bean4.setDay(2);
        bean4.setTime(3);
        bean4.setLength(2);
        bean4.setLocation("FZ319");
        bean4.setName("计算机网络");
        mList.add(bean4);
        CalendarBean bean5 = new CalendarBean();
        bean5.setColor(5);
        bean5.setDay(2);
        bean5.setTime(7);
        bean5.setLength(2);
        bean5.setLocation("FZ319");
        bean5.setName("软件质量保证与测试");
        mList.add(bean5);
        CalendarBean bean6 = new CalendarBean();
        bean6.setColor(2);
        bean6.setDay(3);
        bean6.setTime(3);
        bean6.setLength(2);
        bean6.setLocation("FZ319");
        bean6.setName("计算机组成原理");
        mList.add(bean6);
        CalendarBean bean7 = new CalendarBean();
        bean7.setColor(1);
        bean7.setDay(3);
        bean7.setTime(5);
        bean7.setLength(2);
        bean7.setLocation("FZ319");
        bean7.setName("移动应用开发");
        mList.add(bean7);
        CalendarBean bean8 = new CalendarBean();
        bean8.setColor(6);
        bean8.setDay(3);
        bean8.setTime(7);
        bean8.setLength(2);
        bean8.setLocation("FZ319");
        bean8.setName("数据挖掘");
        mList.add(bean8);
        CalendarBean bean9 = new CalendarBean();
        bean9.setColor(4);
        bean9.setDay(4);
        bean9.setTime(3);
        bean9.setLength(2);
        bean9.setLocation("FZ319");
        bean9.setName("计算机网络");
        mList.add(bean9);
        CalendarBean bean10 = new CalendarBean();
        bean10.setColor(3);
        bean10.setDay(4);
        bean10.setTime(7);
        bean10.setLength(2);
        bean10.setLocation("FZ319");
        bean10.setName("网络编程技术");
        mList.add(bean10);
        CalendarBean bean11 = new CalendarBean();
        bean11.setColor(5);
        bean11.setDay(4);
        bean11.setTime(9);
        bean11.setLength(2);
        bean11.setLocation("FZ319");
        bean11.setName("软件质量保证与测试");
        mList.add(bean11);
        CalendarBean bean12 = new CalendarBean();
        bean12.setColor(6);
        bean12.setDay(5);
        bean12.setTime(5);
        bean12.setLength(1);
        bean12.setLocation("FZ319");
        bean12.setName("数据挖掘");
        mList.add(bean12);
        CalendarBean bean13 = new CalendarBean();
        bean13.setColor(5);
        bean13.setDay(5);
        bean13.setTime(7);
        bean13.setLength(2);
        bean13.setLocation("FZ319");
        bean13.setName("软件质量保证与测试");
        mList.add(bean13);
        mCalendarView.updateSchedule(mList);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mCompactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        mCompactCalendarView.setLocale(TimeZone.getDefault(), Locale.ENGLISH);
        mCompactCalendarView.setShouldDrawDaysHeader(true);
        mCompactCalendarView.setCurrentDayBackgroundColor(getResources().getColor(R.color.yellow));
        mCompactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                setTitle(dateFormat.format(dateClicked));
                mCalendarView.refreshCurrentLayout(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setTitle(dateFormat.format(firstDayOfNewMonth));
                mCalendarView.refreshCurrentLayout(firstDayOfNewMonth);
            }
        });
        setCurrentDate(new Date(System.currentTimeMillis()));
        final ImageView arrow = (ImageView) findViewById(R.id.date_picker_arrow);
        RelativeLayout datePickerButton = (RelativeLayout) findViewById(R.id.date_picker_button);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    ViewCompat.animate(arrow).rotation(0).start();
                    mAppBarLayout.setExpanded(false, true);
                    isExpanded = false;
                } else {
                    ViewCompat.animate(arrow).rotation(180).start();
                    mAppBarLayout.setExpanded(true, true);
                    isExpanded = true;
                }
            }
        });
        mCalendarView = (CalendarView) findViewById(R.id.calendarview_view);
    }

    public void setCurrentDate(Date date) {
        setTitle(dateFormat.format(date));
        if (mCompactCalendarView != null) {
            mCompactCalendarView.setCurrentDate(date);
        }
    }

    public void setTitle(String subtitle) {
        TextView datePickerTextView = (TextView) findViewById(R.id.date_picker_text_view);
        if (datePickerTextView != null) {
            datePickerTextView.setText(subtitle+"月");
        }
    }
}
