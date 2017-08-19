package me.lancer.pocket.tool.mvp.contacts.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.mvp.base.fragment.BaseFragment;
import me.lancer.pocket.tool.mvp.contacts.adapter.ContactAdapter;
import me.lancer.pocket.tool.mvp.contacts.bean.ContactBean;

/**
 * Created by HuangFangzhi on 2017/6/14.
 */

public class ContactFragment extends BaseFragment {

    private RecyclerView rvList;
    private ContactAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<ContactBean> mList = new ArrayList<>();

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
        getContacts();
    }

    private void initView(View view) {
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(mLinearLayoutManager);
        mAdapter = new ContactAdapter(getActivity(), mList, 1);
        rvList.setAdapter(mAdapter);
    }

    private void getContacts() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri,
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
                mList.add(item);
            }
        }
        getActivity().startManagingCursor(cursor);
    }
}