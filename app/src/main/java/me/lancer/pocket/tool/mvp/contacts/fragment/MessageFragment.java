package me.lancer.pocket.tool.mvp.contacts.fragment;

import android.content.Context;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.base.fragment.PresenterFragment;
import me.lancer.pocket.info.mvp.news.INewsView;
import me.lancer.pocket.info.mvp.news.NewsBean;
import me.lancer.pocket.info.mvp.news.NewsPresenter;
import me.lancer.pocket.info.mvp.news.adapter.NewsAdapter;
import me.lancer.pocket.tool.mvp.base.fragment.BaseFragment;
import me.lancer.pocket.tool.mvp.contacts.adapter.ContactAdapter;
import me.lancer.pocket.tool.mvp.contacts.bean.ContactBean;
import me.lancer.pocket.tool.mvp.contacts.bean.MessageBean;

/**
 * Created by HuangFangzhi on 2017/6/14.
 */

public class MessageFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    private ContactAdapter mAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private Map<String, List<MessageBean>> messages = new HashMap<>();
    private List<ContactBean> contacts = new ArrayList<>();
    private List<ContactBean> refences = new ArrayList<>();

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
        getMessages();
//        getRefences();
        getContacts();
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new ContactAdapter(getActivity(), contacts, 2);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getMessages() {
        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = getActivity().getContentResolver().query(uri,
                new String[]{
                        "_id",
                        "address",
                        "person",
                        "body",
                        "date",
                        "type"
                }, null, null, "date desc");
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
            if (messages.get(number) != null) {
                messages.get(number).add(item);
            } else {
                List<MessageBean> items = new ArrayList<MessageBean>();
                items.add(item);
                messages.put(number, items);
            }
        }
        getActivity().startManagingCursor(cursor);
    }

    private void getRefences() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri,
                new String[]{
                        ContactsContract.PhoneLookup.DISPLAY_NAME,
                        "data1",
                        "sort_key"
                }, null, null, "sort_key");
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String name = cursor.getString(0);
            String number = cursor.getString(1);
            ContactBean item = new ContactBean();
            item.setName(name);
            item.setNumber(number);
            refences.add(item);
        }
        getActivity().startManagingCursor(cursor);
    }

    private void getContacts() {
        Iterator iter = messages.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String number = (String) entry.getKey();
            List<MessageBean> message = (List<MessageBean>) entry.getValue();
            ContactBean contact = new ContactBean();
            contact.setMsgs(message);
            contact.setNumber(number);
            contact.setName(number);
            if (message.get(0).getContent().contains("【") && message.get(0).getContent().contains("】")) {
                contact.setName(message.get(0).getContent().split("【")[1].split("】")[0]);
            }
            if (number != null && !number.equals("")) {
                contacts.add(contact);
            }
        }
    }
}