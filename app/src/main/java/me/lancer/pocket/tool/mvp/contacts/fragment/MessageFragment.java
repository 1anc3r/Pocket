package me.lancer.pocket.tool.mvp.contacts.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import me.lancer.pocket.ui.mvp.base.fragment.BaseFragment;
import me.lancer.pocket.tool.mvp.contacts.adapter.ContactAdapter;
import me.lancer.pocket.tool.mvp.contacts.bean.ContactBean;
import me.lancer.pocket.tool.mvp.contacts.bean.MessageBean;

/**
 * Created by HuangFangzhi on 2017/6/14.
 */

public class MessageFragment extends BaseFragment {

    private RecyclerView rvList;
    private ContactAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Map<String, List<MessageBean>> messages = new HashMap<>();
    private List<ContactBean> contacts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_no_swipe, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initData() {
        getMessages();
        getContacts();
    }

    private void initView(View view) {
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        layoutManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(layoutManager);
        adapter = new ContactAdapter(getActivity(), contacts, 2);
        rvList.setAdapter(adapter);
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
                if (messages.get(number) != null) {
                    messages.get(number).add(item);
                } else {
                    List<MessageBean> items = new ArrayList<MessageBean>();
                    items.add(item);
                    messages.put(number, items);
                }
            }
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