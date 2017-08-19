package me.lancer.pocket.tool.mvp.translation.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.tool.mvp.translation.ITranslationView;
import me.lancer.pocket.tool.mvp.translation.TranslationBean;
import me.lancer.pocket.tool.mvp.translation.TranslationPresenter;
import me.lancer.pocket.tool.mvp.translation.adapter.TranslationAdapter;
import me.lancer.pocket.ui.view.ClearEditText;

public class TranslationActivity extends PresenterActivity<TranslationPresenter> implements ITranslationView {

    private TextView tvRes;
    private ClearEditText etTrans;
    private LinearLayout llRes;
    private RecyclerView rvList;
    private TranslationAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<TranslationBean> mList = new ArrayList<>();
    private Map<String, String> mMap = new HashMap<>();

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
                        if (etTrans.getText().length() > 0) {
                            llRes.setVisibility(View.VISIBLE);
                            mList.clear();
                            mList.addAll((List<TranslationBean>) msg.obj);
                            mMap.clear();
                            for (int i = 0; i < mList.size(); i++) {
                                if (mList.get(i).getPos() != null && mList.get(i).getD() != null) {
                                    if (mMap.get(mList.get(i).getPos()) == null) {
                                        mMap.put(mList.get(i).getPos(), mList.get(i).getD());
                                    } else {
                                        mMap.put(mList.get(i).getPos(), mMap.get(mList.get(i).getPos()) + ";" + mList.get(i).getD());
                                    }
                                }
                            }
                            Iterator iter = mMap.entrySet().iterator();
                            int flag = 0;
                            if (mMap.size() > 0) {
                                tvRes.setVisibility(View.VISIBLE);
                                while (iter.hasNext()) {
                                    Map.Entry entry = (Map.Entry) iter.next();
                                    String key = (String) entry.getKey();
                                    String val = (String) entry.getValue();
                                    if (flag == 0) {
                                        tvRes.setText(key + "\t\t" + val.replace(";", " ; "));
                                        flag = 1;
                                    } else {
                                        tvRes.append("\n" + key + "\t\t" + val.replace(";", " ; "));
                                    }
                                }
                            } else {
                                tvRes.setVisibility(View.GONE);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            llRes.setVisibility(View.GONE);
                        }
                    }
                    break;
            }
        }
    };

    private Runnable loadTranslation = new Runnable() {
        @Override
        public void run() {
            presenter.loadTranslation(keyword);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("翻译");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("翻译");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tvRes = (TextView) findViewById(R.id.tv_res);
        etTrans = (ClearEditText) findViewById(R.id.et_trans);
        etTrans.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyword = charSequence.toString();
                if (keyword.length() > 0) {
                    new Thread(loadTranslation).start();
                } else {
                    llRes.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        llRes = (LinearLayout) findViewById(R.id.ll_res);
        rvList = (RecyclerView) findViewById(R.id.rv_res);
        mLinearLayoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(mLinearLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TranslationAdapter(this, mList);
        rvList.setAdapter(mAdapter);
    }

    @Override
    protected TranslationPresenter onCreatePresenter() {
        return new TranslationPresenter(this);
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
    public void showTranslation(List<TranslationBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }
}
