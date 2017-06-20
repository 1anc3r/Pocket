package me.lancer.pocket.tool.mvp.music.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.base.activity.BaseActivity;
import me.lancer.pocket.tool.mvp.file.activity.FileActivity;
import me.lancer.pocket.tool.mvp.music.adapter.MusicAdapter;
import me.lancer.pocket.tool.mvp.music.bean.MusicBean;
import me.lancer.pocket.ui.application.mApp;

public class MusicActivity extends BaseActivity implements View.OnClickListener {

    mApp app;
    private TextView tvShow;
    private ListView lvMusic;
    private EditText etSearch;
    private ProgressDialog mProgressDialog;
    private ImageView ivBack, ivSearch;
    private LinearLayout llBottom, btnDelete, btnCopy, btnMove, btnShare, btnAll;
    private TextView tvDelete, tvCopy, tvMove, tvShare, tvAll;

    private final static int SCAN_OK = 1;

    private MusicAdapter adapter;
    private List<MusicBean> musicList = new ArrayList<>();
    private List<MusicBean> refenList = new ArrayList<>();
    private List<String> posList = new ArrayList<>();
    private List<String> searchList = new ArrayList<>();
    private String searchStr = new String();
    private Handler handler = new Handler();
    private Boolean isAll = false;

    private SharedPreferences pref;
    private String language = "zn";
    private String strConnectionSucceeded = "";
    private String strNoConnection = "";
    private String strConnectionFailed = "";
    private String strShow = "";
    private String strNoInternalExternalStorage = "";
    private String strLoading = "";
    private String strDelete = "";
    private String strCopy = "";
    private String strCut = "";
    private String strUpload = "";
    private String strAll = "";
    private String strPaste = "";
    private String strCancel = "";

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    mProgressDialog.dismiss();
                    Collections.sort(musicList, TitleComparator);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private Handler posHandler = new Handler() {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        iLanguage();
        getMusics();
        init();
    }

    public void iLanguage() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        language = pref.getString(getString(R.string.language_choice), "zn");
        if (language.equals("zn")) {
            strConnectionSucceeded = getResources().getString(R.string.connection_succeeded_zn);
            strNoConnection = getResources().getString(R.string.no_connection_zn);
            strConnectionFailed = getResources().getString(R.string.connection_failed_zn);
            strShow = getResources().getString(R.string.music_zn);
            strNoInternalExternalStorage = getResources().getString(R.string.no_internal_external_storage_zn);
            strLoading = getResources().getString(R.string.loading_zn);
            strDelete = getResources().getString(R.string.delete_zn);
            strCopy = getResources().getString(R.string.copy_zn);
            strCut = getResources().getString(R.string.cut_zn);
            strUpload = getResources().getString(R.string.upload_zn);
            strAll = getResources().getString(R.string.all_zn);
            strPaste = getResources().getString(R.string.paste_zn);
            strCancel = getResources().getString(R.string.cancel_zn);
        } else if (language.equals("en")) {
            strConnectionSucceeded = getResources().getString(R.string.connection_succeeded_en);
            strNoConnection = getResources().getString(R.string.no_connection_en);
            strConnectionFailed = getResources().getString(R.string.connection_failed_en);
            strShow = getResources().getString(R.string.music_en);
            strNoInternalExternalStorage = getResources().getString(R.string.no_internal_external_storage_en);
            strLoading = getResources().getString(R.string.loading_en);
            strDelete = getResources().getString(R.string.delete_en);
            strCopy = getResources().getString(R.string.copy_en);
            strCut = getResources().getString(R.string.cut_en);
            strUpload = getResources().getString(R.string.upload_en);
            strAll = getResources().getString(R.string.all_en);
            strPaste = getResources().getString(R.string.paste_en);
            strCancel = getResources().getString(R.string.cancel_en);
        }
    }

    private void init() {
        app = (mApp) MusicActivity.this.getApplication();
        tvShow = (TextView) findViewById(R.id.tv_show);
        tvShow.setText(strShow);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(this);
        lvMusic = (ListView) findViewById(R.id.lv_music);
        adapter = new MusicAdapter(MusicActivity.this, musicList, posList, searchList, posHandler);
        lvMusic.setAdapter(adapter);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        tvDelete.setText(strDelete);
        tvCopy = (TextView) findViewById(R.id.tv_copy);
        tvCopy.setText(strCopy);
        tvMove = (TextView) findViewById(R.id.tv_cut);
        tvMove.setText(strCut);
        tvAll = (TextView) findViewById(R.id.tv_all);
        tvAll.setText(strAll);
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
        } else if (v == ivSearch) {
            if (musicList.size() > 0) {
                InputMethodManager inputManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                LayoutInflater inflater = LayoutInflater.from(this);
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_search, null);
                final Dialog dialog = new AlertDialog.Builder(MusicActivity.this).create();
                etSearch = (EditText) layout.findViewById(R.id.et_search);
                setSearchTextChanged();
                etSearch.setText(searchStr);
                etSearch.setFocusableInTouchMode(true);
                etSearch.setFocusable(true);
                etSearch.requestFocus();
                etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH
                                || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT
                                || actionId == EditorInfo.IME_ACTION_NONE || actionId == EditorInfo.IME_ACTION_PREVIOUS
                                || actionId == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                            dialog.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(layout);
                WindowManager.LayoutParams lp = window.getAttributes();
                window.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                window.setAttributes(lp);
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        } else if (v == btnDelete) {
            Handler dHandler = new Handler();
            dHandler.post(deleteFile);
        } else if (v == btnAll) {
            Handler aHandler = new Handler();
            aHandler.post(selectAllFile);
        } else if (v == btnCopy) {
            List<String> portal = new ArrayList<>();
            for (int i = 0; i < posList.size(); i++) {
                portal.add(musicList.get(Integer.parseInt(posList.get(i))).getPath());
            }
            Bundle bundle = new Bundle();
            bundle.putString("method", "copy");
            bundle.putStringArrayList("source", (ArrayList<String>) portal);
            Intent intent = new Intent();
            intent.setClass(MusicActivity.this, FileActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v == btnMove) {
            List<String> portal = new ArrayList<>();
            for (int i = 0; i < posList.size(); i++) {
                portal.add(musicList.get(Integer.parseInt(posList.get(i))).getPath());
            }
            Bundle bundle = new Bundle();
            bundle.putString("method", "move");
            bundle.putStringArrayList("source", (ArrayList<String>) portal);
            Intent intent = new Intent();
            intent.setClass(MusicActivity.this, FileActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void getMusics() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            showToast(this, strNoInternalExternalStorage);
            return;
        }
        mProgressDialog = ProgressDialog.show(this, null, strLoading);
        new Thread(new Runnable() {

            @Override
            public void run() {
                Uri mMusicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = MusicActivity.this.getContentResolver();
                Cursor mCursor = mContentResolver.query(mMusicUri, null, null, null, null);
                while (mCursor.moveToNext()) {
                    long id = mCursor.getLong(mCursor
                            .getColumnIndex(MediaStore.Audio.Media._ID));
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));
                    String title = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
                    long albumId = mCursor.getInt(mCursor
                            .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    String album = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
                    String artist = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));

                    MusicBean item = new MusicBean(id, path, title, albumId, album, artist);
                    refenList.add(item);
                    musicList.add(item);
                }
                mCursor.close();
                mHandler.sendEmptyMessage(SCAN_OK);

            }
        }).start();
    }

    private void setSearchTextChanged() {

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                handler.post(changed);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                handler.post(changed);
            }
        });
    }

    private void getContactSub(List<MusicBean> contactSub, String searchStr) {
        int length = refenList.size();
        for (int i = 0; i < length; ++i) {
            if (refenList.get(i).getTitle().contains(searchStr)
                    || refenList.get(i).getArtist().contains(searchStr)) {
                contactSub.add(refenList.get(i));
            }
        }
    }

    Runnable deleteFile = new Runnable() {

        @Override
        public void run() {
            Collections.sort(posList, mComparator);
            for (int i = 0; i < posList.size(); i++) {
                String deletePath = musicList.get(Integer.parseInt(posList.get(i))).getPath();
                File deleteFile = new File(deletePath);
                if (deleteFile.exists() && deleteFile.isFile() && deleteFile.canWrite()) {
                    deleteFile.delete();
                    showToast(MusicActivity.this, "删除成功!");
                } else {
                    showToast(MusicActivity.this, "删除失败!");
                }
            }
            int count = 0;
            for (int i = 0; i < posList.size(); i++) {
                musicList.remove(musicList.get(Integer.parseInt(posList.get(i)) - count));
                count++;
            }
            posList.clear();
            lvMusic.requestLayout();
            adapter.notifyDataSetChanged();
        }
    };

    Runnable selectAllFile = new Runnable() {

        @Override
        public void run() {
            if (isAll == false) {
                posList.clear();
                for (int i = 0; i < musicList.size(); i++) {
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

    Runnable changed = new Runnable() {

        @Override
        public void run() {
            searchStr = etSearch.getText().toString();
            searchList.clear();
            searchList.add(searchStr);
            musicList.clear();
            getContactSub(musicList, searchStr);
            Collections.sort(musicList, TitleComparator);
            adapter.notifyDataSetChanged();
        }
    };

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

    Comparator TitleComparator = new Comparator() {
        public int compare(Object obj1, Object obj2) {
            String str1 = ((MusicBean) obj1).getTitle();
            String str2 = ((MusicBean) obj2).getTitle();
            if (str1.compareTo(str2) < 0)
                return -1;
            else if (str1.compareTo(str2) == 0)
                return 0;
            else if (str1.compareTo(str2) > 0)
                return 1;
            return 0;
        }
    };
}
