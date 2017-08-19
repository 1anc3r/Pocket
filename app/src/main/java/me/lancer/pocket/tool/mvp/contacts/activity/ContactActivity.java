package me.lancer.pocket.tool.mvp.contacts.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.contacts.adapter.ContactAdapter;
import me.lancer.pocket.tool.mvp.contacts.bean.ContactBean;
import me.lancer.pocket.ui.mvp.base.activity.BaseActivity;

public class ContactActivity extends BaseActivity implements View.OnClickListener {

    private CollapsingToolbarLayout layout;
    private ImageView ivCover, ivArg1, ivArg2;
    private TextView tvPhone, tvCallLog;
    private RecyclerView rvList;
    private ContactAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<ContactBean> list = new ArrayList<>();

    private String name, number, img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initData();
        initView();
    }

    private void initData() {
        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");
        img = getIntent().getStringExtra("img");
        getCallLog(number);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.t_phone);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        layout = (CollapsingToolbarLayout) findViewById(R.id.ctl_phone);
        layout.setTitle(name);
        ivCover = (ImageView) findViewById(R.id.iv_cover);
        tvCallLog = (TextView) findViewById(R.id.tv_call_log);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvPhone.setText(number);
        ivArg1 = (ImageView) findViewById(R.id.iv_arg1);
        ivArg1.setOnClickListener(this);
        ivArg2 = (ImageView) findViewById(R.id.iv_arg2);
        ivArg2.setOnClickListener(this);
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        rvList.setLayoutManager(layoutManager);
        rvList.setHasFixedSize(true);
        rvList.setNestedScrollingEnabled(false);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        adapter = new ContactAdapter(this, list, 0);
        adapter.setHasStableIds(true);
        rvList.setAdapter(adapter);
        if (list.size() > 0) {
            tvCallLog.setVisibility(View.VISIBLE);
        } else {
            tvCallLog.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == ivArg1) {
            Uri uri = Uri.parse("tel:" + number);
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            startActivity(intent);
        } else if (view == ivArg2) {
            Intent intent = new Intent();
            intent.setClass(this, MessageActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("number", number);
            startActivity(intent);
        }
    }

    private void getCallLog(String query) {
        Uri uri = CallLog.Calls.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri,
                new String[]{
                        CallLog.Calls.CACHED_NAME,
                        CallLog.Calls.NUMBER,
                        CallLog.Calls.TYPE,
                        CallLog.Calls.DATE,
                        CallLog.Calls.DURATION
                }, "number=?", new String[]{query.replace(" ", "").replace("-", "")}, null);
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                String name = cursor.getString(0);
                String number = cursor.getString(1);
                if (name == null || name.equals("")) {
                    name = number;
                }
                int type = Integer.parseInt(cursor.getString(2));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date callDate = new Date(Long.parseLong(cursor.getString(3)));
                String date = sdf.format(callDate);
                int callDuration = Integer.parseInt(cursor.getString(4));
                int min = callDuration / 60;
                int sec = callDuration % 60;
                String duration = min + " 分 " + sec + " 秒 ";
                ContactBean item = new ContactBean();
                item.setName(name);
                item.setNumber(number);
                item.setType(type);
                item.setDate(date);
                item.setDuration(duration);
                list.add(item);
            }
        }
        startManagingCursor(cursor);
    }
}
