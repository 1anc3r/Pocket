package me.lancer.pocket.tool.mvp.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.app.adapter.AppAdapter;
import me.lancer.pocket.tool.mvp.app.bean.AppBean;
import me.lancer.pocket.tool.mvp.base.activity.BaseActivity;

public class AppActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvShow;
    private ListView lvApp;
    private LinearLayout llProgress;

    private final static int SCAN_OK = 1;

    private AppAdapter adapter;
    private List<AppBean> appList = new ArrayList<>();
    private List<String> posList = new ArrayList<>();

    private SharedPreferences pref;
    private String language = "zn";
    private String show = "";
    private String applicationInfo = "";
    private String applicationName = "";
    private String applicationPackage = "";
    private String uninstall = "";
    private String ok = "";
    private String noInternalExternalStorage = "";
    private String loading = "";

    private Handler lHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    llProgress.setVisibility(View.GONE);
                    lvApp.setVisibility(View.VISIBLE);
                    Collections.sort(appList, AppComparator);
                    adapter = new AppAdapter(AppActivity.this, appList, posList);
                    lvApp.setAdapter(adapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        iLanguage();
        getApp();
        init();
    }

    private void iLanguage() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        language = pref.getString(getString(R.string.language_choice), "zn");
        if (language.equals("zn")) {
            show = getResources().getString(R.string.application_zn);
            applicationInfo = getResources().getString(R.string.application_info_zn);
            applicationName = getResources().getString(R.string.application_name_zn);
            applicationPackage = getResources().getString(R.string.application_package_zn);
            uninstall = getResources().getString(R.string.uninstall_zn);
            ok = getResources().getString(R.string.ok_zn);
            noInternalExternalStorage = getResources().getString(R.string.no_internal_external_storage_zn);
            loading = getResources().getString(R.string.loading_zn);
        } else if (language.equals("en")) {
            show = getResources().getString(R.string.application_en);
            applicationInfo = getResources().getString(R.string.application_info_en);
            applicationName = getResources().getString(R.string.application_name_en);
            applicationPackage = getResources().getString(R.string.application_package_en);
            uninstall = getResources().getString(R.string.uninstall_en);
            ok = getResources().getString(R.string.ok_en);
            noInternalExternalStorage = getResources().getString(R.string.no_internal_external_storage_en);
            loading = getResources().getString(R.string.loading_en);
        }
    }

    private void init() {
        llProgress = (LinearLayout) findViewById(R.id.ll_progress);
        lvApp = (ListView) findViewById(R.id.lv_app);
        lvApp.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (appList.get(position).getPackageName() != null) {
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(appList.get(position).getPackageName());
                    startActivity(LaunchIntent);
                }
            }
        });
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        tvShow = (TextView) findViewById(R.id.tv_show);
        tvShow.setText(show);
    }

    @Override
    public void onClick(View v) {
        if (v == ivBack) {
            setResult(RESULT_OK, null);
            finish();
        }
    }

    private void getApp() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            showSnackbar(lvApp, noInternalExternalStorage);
            return;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
                for (int i = 0; i < packages.size(); i++) {
                    PackageInfo packageInfo = packages.get(i);
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {

                    } else {
                        AppBean appInfo = new AppBean();
                        appInfo.setVersionCode(packageInfo.versionCode);
                        appInfo.setAppName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
                        appInfo.setPackageName(packageInfo.packageName);
                        appInfo.setVersionName(packageInfo.versionName);
                        appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(getPackageManager()));
                        appList.add(appInfo);
                    }
                }
                lHandler.sendEmptyMessage(SCAN_OK);
            }
        }).start();
    }

    Comparator AppComparator = new Comparator() {
        public int compare(Object obj1, Object obj2) {
            AppBean app1 = (AppBean) obj1;
            AppBean app2 = (AppBean) obj2;
            if (app1.getAppName().compareToIgnoreCase(app2.getAppName()) < 0)
                return -1;
            else if (app1.getAppName().compareToIgnoreCase(app2.getAppName()) == 0)
                return 0;
            else if (app1.getAppName().compareToIgnoreCase(app2.getAppName()) > 0)
                return 1;
            return 0;
        }
    };
}
