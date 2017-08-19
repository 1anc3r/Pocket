package me.lancer.pocket.tool.mvp.calendar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.calendar.CalendarBean;
import me.lancer.pocket.tool.mvp.calendar.CalendarPresenter;
import me.lancer.pocket.tool.mvp.calendar.ICalendarView;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.ui.view.ClearEditText;

public class CalendarExeActivity extends PresenterActivity<CalendarPresenter> implements ICalendarView {

    private LinearLayout llCalendar;
    private Toolbar toolbar;
    private ClearEditText etName, etLocation;
    private Spinner sWeek, sStartTime, sEndTime;
    private FloatingActionButton mFab;

    private int type;
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
                        if ((long) msg.obj == -1) {
                            showSnackbar(llCalendar, "操作失败!");
                        } else {
                            showSnackbar(llCalendar, "操作成功!");
                            finish();
                        }
                    }
                    break;
                case 4:
                    if (msg.obj != null) {
                        if ((int) msg.obj == -1) {
                            showSnackbar(llCalendar, "操作失败!");
                        } else {
                            showSnackbar(llCalendar, "操作成功!");
                            finish();
                        }
                    }
                    break;
            }
        }
    };

    private Runnable add = new Runnable() {
        @Override
        public void run() {
            presenter.add(temp);
        }
    };

    private Runnable modify = new Runnable() {
        @Override
        public void run() {
            presenter.modify(temp);
        }
    };

    private Runnable delete = new Runnable() {
        @Override
        public void run() {
            presenter.delete(temp);
        }
    };
    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (parent == sWeek) {
                temp.setDay((int) (id + 1));
            } else if (parent == sStartTime) {
                if (id < 4) {
                    temp.setTime((int) (id + 1));
                } else {
                    temp.setTime((int) (id));
                }
            } else if (parent == sEndTime) {
                if (temp.getTime() < 5) {
                    temp.setLength((int) ((id + 2) - temp.getTime()));
                } else {
                    temp.setLength((int) (id + 1) - temp.getTime());
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_exe);
        initView();
        initData();
    }

    private void initData() {
        temp = (CalendarBean) getIntent().getSerializableExtra("item");
        type = getIntent().getIntExtra("type", 0);
        if (type == 0 && temp == null) {
            temp = new CalendarBean();
            temp.setDay(1);
            temp.setTime(1);
            temp.setLength(0);
            toolbar = initToolbar("添加活动");
        } else if (type == 1 && temp != null) {
            etName.setText(temp.getName());
            etLocation.setText(temp.getLocation());
            toolbar.setTitle(temp.getName());
        }
    }

    private void initView() {
        toolbar = initToolbar("添加活动");
        llCalendar = (LinearLayout) findViewById(R.id.ll_calendar);
        etName = (ClearEditText) findViewById(R.id.et_name);
        etLocation = (ClearEditText) findViewById(R.id.et_location);
        sWeek = (Spinner) findViewById(R.id.s_week);
        sWeek.setOnItemSelectedListener(onItemSelectedListener);
        sStartTime = (Spinner) findViewById(R.id.s_start);
        sStartTime.setOnItemSelectedListener(onItemSelectedListener);
        sEndTime = (Spinner) findViewById(R.id.s_end);
        sEndTime.setOnItemSelectedListener(onItemSelectedListener);
        mFab = (FloatingActionButton) findViewById(R.id.fab_collect);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {
                    if (!etName.getText().toString().equals("") && !etLocation.getText().toString().equals("")) {
                        if (temp.getLength() != 0) {
                            temp.setName(etName.getText().toString());
                            temp.setLocation(etLocation.getText().toString());
                            new Thread(add).start();
                        } else {
                            showSnackbar(llCalendar, "请选择时间!");
                        }
                    } else {
                        showSnackbar(llCalendar, "请输入标题和地点!");
                    }
                } else if (type == 1) {
                    if (!etName.getText().toString().equals("") && !etLocation.getText().toString().equals("")) {
                        temp.setName(etName.getText().toString());
                        temp.setLocation(etLocation.getText().toString());
                        new Thread(modify).start();
                    } else {
                        showSnackbar(llCalendar, "请输入标题和地点!");
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                if (temp != null) {
                    new Thread(delete).start();
                } else {
                    showSnackbar(llCalendar, "操作失败, 对象为空!");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
        msg.what = 4;
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
    }
}
