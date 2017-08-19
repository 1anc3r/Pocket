package me.lancer.pocket.tool.mvp.calendar.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.tool.mvp.calendar.CalendarBean;
import me.lancer.pocket.tool.mvp.calendar.CalendarPresenter;
import me.lancer.pocket.tool.mvp.calendar.ICalendarView;
import me.lancer.pocket.tool.mvp.calendar.view.CalendarView;

public class CalendarActivity extends PresenterActivity<CalendarPresenter> implements ICalendarView {

    private AppBarLayout mAppBarLayout;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("M", Locale.CHINA);
    private CompactCalendarView mCompactCalendarView;
    private CalendarView mCalendarView;
    private FloatingActionButton mFab;

    private List<CalendarBean> list = new ArrayList<>();
    private boolean isExpanded = false;
    private CalendarBean temp;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    if (msg.obj != null) {
                        if ((long)msg.obj == -1){
                            showSnackbar(mCalendarView, "操作失败!");
                        } else {
                            showSnackbar(mCalendarView, "操作成功!");
                        }
                    }
                    break;
                case 4:
                    if (msg.obj != null) {
                        list.clear();
                        list.addAll((List<CalendarBean>) msg.obj);
                        mCalendarView.updateCalendar(list);
                    }
                    break;
            }
        }
    };

    private Runnable query = new Runnable() {
        @Override
        public void run() {
            presenter.query();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();
        initData();
    }

    private void initData() {
        new Thread(query).start();
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
        mCompactCalendarView.setUseThreeLetterAbbreviation(true);
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
        mCalendarView.setOnCalendarItemClickListener(new CalendarView.OnCalendarItemClickListener() {
            @Override
            public void onCalendarItemClick(TextView tv, int time, int day, CalendarBean bean) {
                temp = bean;
                Intent intent = new Intent();
                intent.putExtra("type", 1);
                intent.putExtra("item", (Serializable) temp);
                intent.setClass(CalendarActivity.this, CalendarExeActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        mFab = (FloatingActionButton) findViewById(R.id.fab_collect);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("type", 0);
                intent.setClass(CalendarActivity.this, CalendarExeActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                new Thread(query).start();
                break;
            default:
                break;
        }
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

    @Override
    protected CalendarPresenter onCreatePresenter() {
        return new CalendarPresenter(this);
    }

    @Override
    public void showMsg(String log) {
        Message msg = new Message();
        msg.what = 2;
        msg.obj = log;
        handler.sendMessage(msg);
    }

    @Override
    public void showLoad() {
        Message msg = new Message();
        msg.what = 1;
        handler.sendMessage(msg);
    }

    @Override
    public void hideLoad() {
        Message msg = new Message();
        msg.what = 0;
        handler.sendMessage(msg);
    }

    @Override
    public void showResult(int result) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = result;
        handler.sendMessage(msg);
    }

    @Override
    public void showResult(long result) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = result;
        handler.sendMessage(msg);
    }

    @Override
    public void showList(List<CalendarBean> list) {
        Message msg = new Message();
        msg.what = 4;
        msg.obj = list;
        handler.sendMessage(msg);
    }
}
