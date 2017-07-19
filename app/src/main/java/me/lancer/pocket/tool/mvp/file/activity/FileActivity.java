package me.lancer.pocket.tool.mvp.file.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.base.activity.BaseActivity;
import me.lancer.pocket.tool.mvp.file.adapter.FileAdapter;
import me.lancer.pocket.tool.mvp.file.bean.FileBean;
import me.lancer.pocket.mainui.application.App;
import me.lancer.pocket.util.FileTypeRefereeUtil;

public class FileActivity extends BaseActivity implements View.OnClickListener {

    App app;
    private TextView tvPath, tvShow;
    private ListView lvFile;
    private EditText etSearch;
    private Button btnPaste, btnCancell;
    private ImageView ivBack, ivSearch;
    private LinearLayout llBottom, btnDelete, btnCopy, btnMove, btnShare, btnAll;
    private TextView tvDelete, tvCopy, tvMove, tvShare, tvAll;

    private final static int SCAN_OK = 1;

    private FileAdapter adapter;
    private List<FileBean> fileList = new ArrayList<>();
    private List<FileBean> refenList = new ArrayList<>();
    private List<String> posList = new ArrayList<>();
    private List<String> srcList = new ArrayList<>();
    private List<String> searchList = new ArrayList<>();
    private String searchStr = new String();
    private Handler handler = new Handler();
    private String method;
    private String parentpath;
    private File parentfile;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Boolean isAll = false;

    private SharedPreferences pref;

    private Handler lHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    Collections.sort(fileList, NameComparator);
                    if (method.equals("in") || method.equals("out") || method.equals("download")) {
                        llBottom.setVisibility(View.GONE);
                    }
                    lvFile.requestLayout();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (posList.contains(msg.obj)) {
                posList.remove(msg.obj);
            } else {
                posList.add((String) msg.obj);
                Collections.sort(posList, PosComparator);
            }
            if (posList.isEmpty()) {
                if (method.equals("in") || method.equals("out") || method.equals("download")) {
                    llBottom.setVisibility(View.GONE);
                }
            } else {
                llBottom.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        init();
    }

    private void init() {
        app = (App) FileActivity.this.getApplication();
        tvShow = (TextView) findViewById(R.id.tv_show);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        method = b.getString("method");
        srcList = b.getStringArrayList("source");
        if (method.equals("download")) {
            parentpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            tvShow.setText(getResources().getString(R.string.download_zn));
        } else if (method.equals("out")) {
            parentpath = "/mnt/ext_sdcard/";
            tvShow.setText(getResources().getString(R.string.external_zn));
        } else {
            parentpath = Environment.getExternalStorageDirectory().getAbsolutePath();
            tvShow.setText(getResources().getString(R.string.internal_zn));
        }
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(this);
        tvPath = (TextView) findViewById(R.id.tv_path);
        lvFile = (ListView) findViewById(R.id.lv_file);
        adapter = new FileAdapter(FileActivity.this, fileList, posList, searchList, mHandler);
        lvFile.setAdapter(adapter);
        lvFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (new File(fileList.get(position).getPath()).isDirectory()) {
                    parentpath = fileList.get(position).getPath();
                    tvPath.setText(parentpath);
                    fileList.clear();
                    posList.clear();
                    lHandler.post(getFile);
                    lvFile.requestLayout();
                    adapter.notifyDataSetChanged();
                } else {
                    openFile(new File(fileList.get(position).getPath()));
                }
            }
        });
        tvPath.setText(parentpath);
        lHandler.post(getFile);
        if (method.equals("in") || method.equals("out") || method.equals("download") || method.equals("download")) {
            llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        } else if (method.equals("copy") || method.equals("move")) {
            llBottom = (LinearLayout) findViewById(R.id.ll_paste);
            llBottom.setVisibility(View.VISIBLE);
        }
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
        btnPaste = (Button) findViewById(R.id.btn_paste);
        btnPaste.setText(getResources().getString(R.string.paste_zn));
        btnPaste.setOnClickListener(this);
        btnCancell = (Button) findViewById(R.id.btn_cancell);
        btnCancell.setText(getResources().getString(R.string.cancel_zn));
        btnCancell.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ivBack) {
            setResult(RESULT_OK, null);
            finish();
        } else if (v == ivSearch) {
            if (fileList.size() > 0) {
                InputMethodManager inputManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                LayoutInflater inflater = LayoutInflater.from(this);
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_search, null);
                final Dialog dialog = new AlertDialog.Builder(FileActivity.this).create();
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
                portal.add(fileList.get(Integer.parseInt(posList.get(i))).getPath());
            }
            Bundle bundle = new Bundle();
            bundle.putString("method", "copy");
            bundle.putStringArrayList("source", (ArrayList<String>) portal);
            Intent intent = new Intent();
            intent.setClass(FileActivity.this, FileActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v == btnMove) {
            List<String> portal = new ArrayList<>();
            for (int i = 0; i < posList.size(); i++) {
                portal.add(fileList.get(Integer.parseInt(posList.get(i))).getPath());
            }
            Bundle bundle = new Bundle();
            bundle.putString("method", "move");
            bundle.putStringArrayList("source", (ArrayList<String>) portal);
            Intent intent = new Intent();
            intent.setClass(FileActivity.this, FileActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v == btnPaste) {
            if (method.equals("copy")) {
                for (int i = 0; i < srcList.size(); i++) {
                    copyFile(srcList.get(i), new File(srcList.get(i)).getName(), parentpath);
                }
            } else if (method.equals("move")) {
                for (int i = 0; i < srcList.size(); i++) {
                    moveFile(srcList.get(i), new File(srcList.get(i)).getName(), parentpath);
                }
            }
            Collections.sort(fileList, NameComparator);
            posList.clear();
            lvFile.requestLayout();
            adapter.notifyDataSetChanged();
        } else if (v == btnCancell) {
            setResult(RESULT_OK, null);
            finish();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Handler bHandler = new Handler();
            bHandler.post(back2parent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void copyFile(String inputPath, String inputFile, String outputPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            File input = new File(inputPath);
            if (input.isFile()) {
                fileList.add(new FileBean(outputPath + "/" + inputFile,
                        inputFile, outputPath, new ArrayList<String>(),
                        format.format(new Date((new File(outputPath + "/" + inputFile)).lastModified()))));
                in = new FileInputStream(inputPath);
                out = new FileOutputStream(outputPath + "/" + inputFile);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
                showSnackbar(lvFile, "复制成功!");
            } else if (input.isDirectory()) {
                File output = new File(outputPath + "/" + input.getName());
                if (!output.exists()) {
                    output.mkdirs();
                }
                File[] list = input.listFiles();
                if (list.length != 0) {
                    for (File item : list) {
                        copyFile(item.getPath(), item.getName(), output.getPath());
                    }
                }
                showSnackbar(lvFile, "复制成功!");
            } else {
                showSnackbar(lvFile, "复制失败!");
            }
        } catch (FileNotFoundException fnfe1) {
            showSnackbar(lvFile, "复制失败:" + fnfe1.getMessage());
        } catch (Exception e) {
            showSnackbar(lvFile, "复制失败:" + e.getMessage());
        }
    }

    private void moveFile(String inputPath, String inputFile, String outputPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            File input = new File(inputPath);
            if (input.isFile()) {
                fileList.add(new FileBean(outputPath + "/" + inputFile,
                        inputFile, outputPath, new ArrayList<String>(),
                        format.format(new Date((new File(outputPath + "/" + inputFile)).lastModified()))));
                File dir = new File(outputPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                in = new FileInputStream(inputPath);
                out = new FileOutputStream(outputPath + "/" + inputFile);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
                new File(inputPath).delete();
                showSnackbar(lvFile, "剪切成功!");
            } else if (input.isDirectory()) {
                File output = new File(outputPath + "/" + input.getName());
                if (!output.exists()) {
                    output.mkdirs();
                }
                File[] list = input.listFiles();
                if (list.length != 0) {
                    for (File item : list) {
                        moveFile(item.getPath(), item.getName(), output.getPath());
                    }
                }
                showSnackbar(lvFile, "剪切成功!");
            } else {
                showSnackbar(lvFile, "剪切失败!");
            }
        } catch (FileNotFoundException fnfe1) {
            showSnackbar(lvFile, "剪切失败:" + fnfe1.getMessage());
        } catch (Exception e) {
            showSnackbar(lvFile, "剪切失败:" + e.getMessage());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] list = dir.list();
            for (int i = 0; i < list.length; i++) {
                boolean success = deleteDir(new File(dir, list[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    private void openFile(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        startActivity(intent);
    }

    private String getType(File file) {
        String type = "*/*";
        String fName = file.getName();
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        for (int i = 0; i < FileTypeRefereeUtil.FILE_TYPE_TABLE.length; i++) {
            if (end.equals(FileTypeRefereeUtil.FILE_TYPE_TABLE[i][0]))
                type = FileTypeRefereeUtil.FILE_TYPE_TABLE[i][1];
        }
        return type;
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

    private void getContactSub(List<FileBean> contactSub, String searchStr) {
        int length = refenList.size();
        for (int i = 0; i < length; ++i) {
            if (refenList.get(i).getFileName().contains(searchStr)) {
                contactSub.add(refenList.get(i));
            }
        }
    }

    Runnable selectAllFile = new Runnable() {

        @Override
        public void run() {
            if (isAll == false) {
                posList.clear();
                for (int i = 0; i < fileList.size(); i++) {
                    posList.add("" + i);
                }
                isAll = true;
                llBottom.setVisibility(View.VISIBLE);
            } else {
                posList.clear();
                isAll = false;
                if (method.equals("in") || method.equals("out") || method.equals("download")) {
                    llBottom.setVisibility(View.GONE);
                }
            }
            lvFile.requestLayout();
            adapter.notifyDataSetChanged();
        }
    };

    Runnable getFile = new Runnable() {

        @Override
        public void run() {
            fileList.clear();
            File root = new File(parentpath);
            if (root.isDirectory()) {
                File fileis[] = root.listFiles();
                if (fileis != null) {
                    for (File filei : fileis) {
                        if (filei.isDirectory() && filei.exists() && filei.canRead() && filei.canWrite()) {
                            File fileiis[] = filei.listFiles();
                            List<String> childs = new ArrayList<>();
                            for (File fileii : fileiis) {
                                if (fileiis != null) {
                                    if (filei.exists() && filei.canRead() && filei.canWrite()) {
                                        childs.add(fileii.getAbsolutePath());
                                    }
                                }
                            }
                            fileList.add(new FileBean(filei.getAbsolutePath(), filei.getName(), parentpath, childs,
                                    format.format(new Date((new File(filei.getAbsolutePath())).lastModified()))));
                            refenList.add(new FileBean(filei.getAbsolutePath(), filei.getName(), parentpath, childs,
                                    format.format(new Date((new File(filei.getAbsolutePath())).lastModified()))));
                        } else if (filei.exists() && filei.isFile() && filei.canRead() && filei.canWrite()) {
                            List<String> childs = new ArrayList<>();
                            fileList.add(new FileBean(filei.getAbsolutePath(), filei.getName(), parentpath, childs,
                                    format.format(new Date((new File(filei.getAbsolutePath())).lastModified()))));
                            refenList.add(new FileBean(filei.getAbsolutePath(), filei.getName(), parentpath, childs,
                                    format.format(new Date((new File(filei.getAbsolutePath())).lastModified()))));
                        }
                    }
                }
            }
            lHandler.sendEmptyMessage(SCAN_OK);
        }
    };

    Runnable back2parent = new Runnable() {

        @Override
        public void run() {
            if (parentpath.equals(Environment.getExternalStorageDirectory().getAbsolutePath()) || parentpath.equals("/mnt/ext_sdcard") || parentpath.equals("/mnt/ext_sdcard/")) {
                setResult(RESULT_OK, null);
                finish();
            } else {
                parentfile = new File(parentpath);
                parentpath = parentfile.getParent();
                tvPath.setText(parentpath);
                fileList.clear();
                posList.clear();
                lHandler.post(getFile);
            }
        }
    };

    Runnable deleteFile = new Runnable() {

        @Override
        public void run() {
            Collections.sort(posList, PosComparator);
            for (int i = 0; i < posList.size(); i++) {
                String deletePath = fileList.get(Integer.parseInt(posList.get(i))).getPath();
                File deleteFile = new File(deletePath);
                if (deleteFile.isFile()) {
                    if (deleteFile.exists() && deleteFile.isFile() && deleteFile.canWrite()) {
                        deleteFile.delete();
                        showSnackbar(lvFile, "删除成功!");
                    } else {
                        showSnackbar(lvFile, "删除失败!");
                    }
                } else if (deleteFile.isDirectory()) {
                    deleteDir(deleteFile);
                }
            }
            int count = 0;
            for (int i = 0; i < posList.size(); i++) {
                fileList.remove(fileList.get(Integer.parseInt(posList.get(i)) - count));
                count++;
            }
            posList.clear();
            lvFile.requestLayout();
            adapter.notifyDataSetChanged();
        }
    };

    Runnable changed = new Runnable() {

        @Override
        public void run() {
            searchStr = etSearch.getText().toString();
            searchList.clear();
            searchList.add(searchStr);
            fileList.clear();
            getContactSub(fileList, searchStr);
            Collections.sort(fileList, NameComparator);
            adapter.notifyDataSetChanged();
        }
    };

    Comparator PosComparator = new Comparator() {
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

    Comparator NameComparator = new Comparator() {
        public int compare(Object obj1, Object obj2) {
            FileBean file1 = (FileBean) obj1;
            FileBean file2 = (FileBean) obj2;
            if (file1.getFileName().compareToIgnoreCase(file2.getFileName()) < 0)
                return -1;
            else if (file1.getFileName().compareToIgnoreCase(file2.getFileName()) == 0)
                return 0;
            else if (file1.getFileName().compareToIgnoreCase(file2.getFileName()) > 0)
                return 1;
            return 0;
        }
    };
}
