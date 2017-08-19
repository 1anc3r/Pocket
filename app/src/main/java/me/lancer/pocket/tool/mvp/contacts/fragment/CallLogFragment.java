package me.lancer.pocket.tool.mvp.contacts.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.mvp.base.fragment.BaseFragment;
import me.lancer.pocket.tool.mvp.contacts.adapter.ContactAdapter;
import me.lancer.pocket.tool.mvp.contacts.bean.ContactBean;

/**
 * Created by HuangFangzhi on 2017/6/14.
 */

public class CallLogFragment extends BaseFragment {

    private RecyclerView rvList;
    private ContactAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<ContactBean> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_no_swipe, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initView(view);
    }

    private void initData() {
        getCallLog();
    }

    private void initView(View view) {
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        layoutManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(layoutManager);
        adapter = new ContactAdapter(getActivity(), list, 0);
        rvList.setAdapter(adapter);
    }

    private void getCallLog() {
        Uri uri = CallLog.Calls.CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri,
                new String[]{
                        CallLog.Calls.CACHED_NAME,
                        CallLog.Calls.NUMBER,
                        CallLog.Calls.TYPE,
                        CallLog.Calls.DATE,
                        CallLog.Calls.DURATION
                }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
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
        getActivity().startManagingCursor(cursor);
    }
}