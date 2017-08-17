package me.lancer.pocket.tool.mvp.contacts.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.mvp.base.activity.BaseActivity;
import me.lancer.pocket.tool.mvp.contacts.adapter.ContactAdapter;
import me.lancer.pocket.tool.mvp.contacts.bean.ContactBean;

public class ContactSearchActivity extends BaseActivity {

    Toolbar toolbar;

    private RecyclerView mRecyclerView;

    private ContactAdapter mAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private List<ContactBean> mList = new ArrayList<>();

    private String keyword;

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
                        mList.clear();
                        mList.addAll((List<ContactBean>) msg.obj);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_no_swipe);
        initData();
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("搜索结果");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_result);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new ContactAdapter(this, mList, 1);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        keyword = getIntent().getStringExtra("query");
        getContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("搜索...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keyword = query;
                getContacts();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                showAboutDialog();
                break;
        }
        return true;
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("电话、通讯录、信息");
        builder.setMessage(
                "\t\t\t\t/*\n" +
                "\t\t\t\t * 电话 : 列出手机上的通话记录\n" +
                "\t\t\t\t * 通讯录 : 列出手机上的联系人\n" +
                "\t\t\t\t * 信息 : 列出手机上的短信记录\n" +
                "\t\t\t\t */");
        builder.show();
    }

    private void getContacts() {
        List<ContactBean> temp = new ArrayList<>();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri,
                new String[]{
                        ContactsContract.PhoneLookup.DISPLAY_NAME,
                        "data1",
                        "sort_key"
                }, null, null, "sort_key");
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                String name = cursor.getString(0);
                String number = cursor.getString(1);
                ContactBean item = new ContactBean();
                item.setName(name);
                item.setNumber(number);
                if (name.contains(keyword) || number.contains(keyword)) {
                    temp.add(item);
                }
            }
        }
        Message msg = new Message();
        msg.what = 3;
        msg.obj = temp;
        handler.sendMessage(msg);
    }
}
