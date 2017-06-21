package me.lancer.pocket.tool.mvp.contacts.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.base.activity.BaseActivity;
import me.lancer.pocket.tool.mvp.contacts.adapter.MessageAdapter;
import me.lancer.pocket.tool.mvp.contacts.bean.MessageBean;

public class MessageActivity extends BaseActivity {

    private EditText etMsg;
    private Button btnSend;
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<MessageBean> mList = new ArrayList<>();

    private String name, number, action = "me.lancer.pocket";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    showSnackbar(mRecyclerView, "发送成功");
                    mList.clear();
                    getMessages(number);
                    mAdapter.notifyDataSetChanged();
                    etMsg.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    break;
                case 1:
                    showSnackbar(mRecyclerView, "发送失败");
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initData();
        initView();
    }

    private void initData() {
        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");
        getMessages(number);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (name != null) {
            toolbar.setTitle(name);
        } else {
            toolbar.setTitle(number);
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_chat);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MessageAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
        if (mList.size() > 0) {
            mRecyclerView.scrollToPosition(0);
        }
        etMsg = (EditText) findViewById(R.id.et_msg);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(number, etMsg.getText().toString());
            }
        });
    }

    private void getMessages(String query) {
        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = getContentResolver().query(uri,
                new String[]{
                        "_id",
                        "address",
                        "person",
                        "body",
                        "date",
                        "type"
                }, "address=?", new String[]{query.replace(" ", "").replace("-", "")}, "date desc");
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String number = cursor.getString(1);
            String body = cursor.getString(3);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date msgDate = new Date(Long.parseLong(cursor.getString(4)));
            String date = sdf.format(msgDate);
            int type = Integer.parseInt(cursor.getString(5));
            MessageBean item = new MessageBean();
            item.setContent(body);
            item.setNumber(number);
            item.setType(type);
            item.setDate(date);
            mList.add(item);
        }
        startManagingCursor(cursor);
    }

    private void sendMessage(String number, String message) {
        String phoneNumber = number;
        String smsContent = message;
        sendSms(phoneNumber, smsContent);
        writeToDataBase(phoneNumber, smsContent);
        Message msg = new Message();
        msg.what = 0;
        handler.sendMessage(msg);
    }

    private void sendSms(String phoneNumber, String smsContent) {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> messageArray = smsManager.divideMessage(smsContent);
        smsManager.sendMultipartTextMessage(phoneNumber, null, messageArray, null, null);
    }

    private void writeToDataBase(String phoneNumber, String smsContent) {
        ContentValues values = new ContentValues();
        values.put("address", phoneNumber);
        values.put("body", smsContent);
        values.put("type", "2");
        values.put("read", "1");
        getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
    }
}
