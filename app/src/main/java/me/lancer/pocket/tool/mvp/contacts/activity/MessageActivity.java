package me.lancer.pocket.tool.mvp.contacts.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
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
import me.lancer.pocket.tool.mvp.contacts.adapter.MessageAdapter;
import me.lancer.pocket.tool.mvp.contacts.bean.MessageBean;
import me.lancer.pocket.ui.mvp.base.activity.BaseActivity;

public class MessageActivity extends BaseActivity {

    private EditText etMsg;
    private Button btnSend;
    private RecyclerView rvList;
    private MessageAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<MessageBean> list = new ArrayList<>();

    private String name, number, action = "me.lancer.pocket";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    showSnackbar(rvList, "发送成功");
                    list.clear();
                    getMessages(number);
                    adapter.notifyDataSetChanged();
                    etMsg.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    break;
                case 1:
                    showSnackbar(rvList, "发送失败");
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
        rvList = (RecyclerView) findViewById(R.id.rv_chat);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        adapter = new MessageAdapter(this, list);
        rvList.setAdapter(adapter);
        if (list.size() > 0) {
            rvList.scrollToPosition(0);
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
        if (cursor != null) {
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
                list.add(item);
            }
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
