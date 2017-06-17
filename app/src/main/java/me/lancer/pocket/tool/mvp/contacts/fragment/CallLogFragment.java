package me.lancer.pocket.tool.mvp.contacts.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.base.fragment.PresenterFragment;
import me.lancer.pocket.info.mvp.news.INewsView;
import me.lancer.pocket.info.mvp.news.NewsBean;
import me.lancer.pocket.info.mvp.news.NewsPresenter;
import me.lancer.pocket.info.mvp.news.adapter.NewsAdapter;
import me.lancer.pocket.tool.mvp.base.fragment.BaseFragment;
import me.lancer.pocket.tool.mvp.contacts.adapter.ContactAdapter;
import me.lancer.pocket.tool.mvp.contacts.bean.ContactBean;

/**
 * Created by HuangFangzhi on 2017/6/14.
 */

public class CallLogFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    private ContactAdapter mAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private List<ContactBean> mList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_no_swip, container, false);
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new ContactAdapter(getActivity(), mList, 0);
        mRecyclerView.setAdapter(mAdapter);
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
            mList.add(item);
        }
        getActivity().startManagingCursor(cursor);
    }
}