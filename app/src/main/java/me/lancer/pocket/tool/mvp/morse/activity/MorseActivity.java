package me.lancer.pocket.tool.mvp.morse.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.morse.IMorseView;
import me.lancer.pocket.tool.mvp.morse.MorsePresenter;
import me.lancer.pocket.tool.mvp.morse.adapter.MorseAdapter;
import me.lancer.pocket.ui.mvp.base.activity.PresenterActivity;
import me.lancer.pocket.ui.view.ClearEditText;

@SuppressWarnings("ALL")
public class MorseActivity extends PresenterActivity<MorsePresenter> implements IMorseView, View.OnClickListener {

    private Toolbar toolbar;
    private ClearEditText etChar, etCode;
    private FloatingActionButton fab;
    private RecyclerView rvList;
    private MorseAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;

    private List<String> list = new ArrayList<>();
    private Map<String, String> ch2co = new HashMap<>();
    private Map<String, String> co2ch = new HashMap<>();

    private String content;
    private long charTime, codeTime;
    private Runnable loadFile = new Runnable() {
        @Override
        public void run() {
            AssetManager assetManager = MorseActivity.this.getAssets();
            try {
                InputStream is = assetManager.open("morsealphabet.json");
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuffer stringBuffer = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    stringBuffer.append(temp);
                }
                content = stringBuffer.toString();
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable loadMorse = new Runnable() {
        @Override
        public void run() {
            presenter.loadMorse(content);
        }
    };
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
                        ch2co.clear();
                        ch2co.putAll((Map<? extends String, ? extends String>) msg.obj);
                        Iterator iter = ch2co.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            String ch = (String) entry.getKey();
                            String co = (String) entry.getValue();
                            list.add(ch + " = " + co);
                            Collections.sort(list, new mComparator());
                            co2ch.put(co, ch);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 4:
                    new Thread(loadMorse).start();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse);
        initView();
        initData();
    }

    private void initData() {
        new Thread(loadFile).start();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("摩斯电码");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        etChar = (ClearEditText) findViewById(R.id.et_char);
        etChar.setOnClickListener(this);
        etCode = (ClearEditText) findViewById(R.id.et_code);
        etCode.setOnClickListener(this);
        fab = (FloatingActionButton) findViewById(R.id.fab_collect);
        fab.setOnClickListener(this);
        rvList = (RecyclerView) findViewById(R.id.rv_morse);
        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        adapter = new MorseAdapter(this, list);
        rvList.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view == fab) {
            String ch = etChar.getText().toString().toUpperCase();
            String co = etCode.getText().toString().toUpperCase();
            String out = "";
            if (ch.length() > 0 && co.length() == 0) {
                for (int i = 0; i < ch.length(); i++) {
                    String itm = String.valueOf(ch.charAt(i));
                    if (ch2co.get(itm) != null) {
                        out += ch2co.get(itm);
                    }
                }
                etCode.setText(out);
            } else if (co.length() > 0 && ch.length() == 0) {
                String[] arr = co.split(" ");
                for (String itm : arr) {
                    itm = itm.replace(" ", "") + " ";
                    if (co2ch.get(itm) != null) {
                        out += co2ch.get(itm);
                    }
                }
                etChar.setText(out);
            }
        } else if (view == etChar) {
            if ((System.currentTimeMillis() - charTime) > 2000) {
                charTime = System.currentTimeMillis();
            } else {
                onClickCopy(etChar);
            }
        } else if (view == etCode) {
            if ((System.currentTimeMillis() - codeTime) > 2000) {
                codeTime = System.currentTimeMillis();
            } else {
                onClickCopy(etCode);
            }
        }
    }

    public void onClickCopy(EditText et) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (et.getText().toString().length() > 0) {
            cm.setText(et.getText());
            showSnackbar(rvList, "复制成功(ง •̀_•́)ง");
        } else {
            showSnackbar(rvList, "没有内容(๑•́ ₃•̀๑)");
        }
    }

    @Override
    protected MorsePresenter onCreatePresenter() {
        return new MorsePresenter(this);
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
    public void showMorse(Map<String, String> map) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = map;
        handler.sendMessage(msg);
    }

    private class mComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            String s1 = (String) o1;
            String s2 = (String) o2;
            return s1.compareTo(s2);
        }
    }
}
