package me.lancer.pocket.tool.mvp.image.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.file.activity.FileActivity;
import me.lancer.pocket.tool.mvp.image.adapter.ImageIAdapter;
import me.lancer.pocket.ui.application.App;
import me.lancer.pocket.ui.mvp.base.activity.BaseActivity;

public class ImageIActivity extends BaseActivity implements View.OnClickListener {

    App app;
    Comparator mComparator = new Comparator() {
        public int compare(Object obj1, Object obj2) {
            String str1 = (String) obj1;
            String str2 = (String) obj2;
            if (Integer.parseInt(str1) < Integer.parseInt(str2))
                return -1;
            else if (Integer.parseInt(str1) == Integer.parseInt(str2))
                return 0;
            else if (Integer.parseInt(str1) > Integer.parseInt(str2))
                return 1;
            return 0;
        }
    };
    private ImageView ivBack;
    private TextView tvShow;
    private GridView mGridView;
    private LinearLayout llBottom, btnDelete, btnCopy, btnMove, btnAll;
    private TextView tvDelete, tvCopy, tvMove, tvAll;
    private ImageIAdapter adapter;
    private List<String> posList = new ArrayList<>();
    public Handler iHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (posList.contains(msg.obj)) {
                posList.remove(msg.obj);
            } else {
                posList.add((String) msg.obj);
                Collections.sort(posList, mComparator);
            }
            if (posList.isEmpty()) {
                llBottom.setVisibility(View.GONE);
            } else {
                llBottom.setVisibility(View.VISIBLE);
            }
        }
    };
    private List<String> picList = new ArrayList<>();
    Runnable deleteFile = new Runnable() {

        @Override
        public void run() {
            int count = 0;
            Collections.sort(posList, mComparator);
            while (!posList.isEmpty()) {
                String deletePath = picList.get(Integer.parseInt(posList.get(0)) - count);
                File deleteFile = new File(deletePath);
                if (deleteFile.exists() && deleteFile.isFile() && deleteFile.canWrite()) {
                    deleteFile.delete();
                    picList.remove(deletePath);
                    posList.remove(posList.get(0));
                    showSnackbar(mGridView, "删除成功!");
                } else {
                    showSnackbar(mGridView, "删除失败!");
                }
                count++;
            }
            adapter.notifyDataSetChanged();
        }
    };
    private Boolean isAll = false;
    Runnable selectAllFile = new Runnable() {

        @Override
        public void run() {
            if (!isAll) {
                posList.clear();
                for (int i = 0; i < picList.size(); i++) {
                    posList.add("" + i);
                }
                isAll = true;
                llBottom.setVisibility(View.VISIBLE);
            } else {
                posList.clear();
                isAll = false;
                llBottom.setVisibility(View.GONE);
            }
            adapter.notifyDataSetChanged();
        }
    };
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_i);
        init();
    }

    private void init() {
        app = (App) ImageIActivity.this.getApplication();
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        Intent i = getIntent();
        tvShow = (TextView) findViewById(R.id.tv_show);
        tvShow.setText("" + i.getExtras().get("title"));
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        mGridView = (GridView) findViewById(R.id.gv_child);
        picList = getIntent().getStringArrayListExtra("data");
        Collections.reverse(picList);
        adapter = new ImageIAdapter(this, picList, posList, mGridView, iHandler);
        mGridView.setAdapter(adapter);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        tvDelete.setText(getResources().getString(R.string.delete_zn));
        tvCopy = (TextView) findViewById(R.id.tv_copy);
        tvCopy.setText(getResources().getString(R.string.copy_zn));
        tvMove = (TextView) findViewById(R.id.tv_cut);
        tvMove.setText(getResources().getString(R.string.cut_zn));
        tvAll = (TextView) findViewById(R.id.tv_all);
        tvAll.setText(getResources().getString(R.string.all_zn));
        btnDelete = (LinearLayout) findViewById(R.id.btn_del);
        btnDelete.setOnClickListener(this);
        btnCopy = (LinearLayout) findViewById(R.id.btn_copy);
        btnCopy.setOnClickListener(this);
        btnMove = (LinearLayout) findViewById(R.id.btn_move);
        btnMove.setOnClickListener(this);
        btnAll = (LinearLayout) findViewById(R.id.btn_all);
        btnAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ivBack) {
            setResult(RESULT_OK, null);
            finish();
        } else if (v == btnDelete) {
            Handler dHandler = new Handler();
            dHandler.post(deleteFile);
        } else if (v == btnAll) {
            Handler aHandler = new Handler();
            aHandler.post(selectAllFile);
        } else if (v == btnCopy) {
            List<String> portal = new ArrayList<>();
            for (int i = 0; i < posList.size(); i++) {
                portal.add(picList.get(Integer.parseInt(posList.get(i))));
            }
            Bundle bundle = new Bundle();
            bundle.putString("method", "copy");
            bundle.putStringArrayList("source", (ArrayList<String>) portal);
            Intent intent = new Intent();
            intent.setClass(ImageIActivity.this, FileActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v == btnMove) {
            List<String> portal = new ArrayList<>();
            for (int i = 0; i < posList.size(); i++) {
                portal.add(picList.get(Integer.parseInt(posList.get(i))));
            }
            Bundle bundle = new Bundle();
            bundle.putString("method", "move");
            bundle.putStringArrayList("source", (ArrayList<String>) portal);
            Intent intent = new Intent();
            intent.setClass(ImageIActivity.this, FileActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
