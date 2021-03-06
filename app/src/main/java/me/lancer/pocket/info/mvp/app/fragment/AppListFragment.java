package me.lancer.pocket.info.mvp.app.fragment;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.app.AppBean;
import me.lancer.pocket.info.mvp.app.AppPresenter;
import me.lancer.pocket.info.mvp.app.IAppView;
import me.lancer.pocket.info.mvp.app.adapter.AppGridAdapter;
import me.lancer.pocket.ui.mvp.base.fragment.PresenterLazyLoadFragment;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class AppListFragment extends PresenterLazyLoadFragment<AppPresenter> implements IAppView {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvList;
    private AppGridAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private List<AppBean> list = new ArrayList<>();
    private int type = 0, pager = 1, last = 0, load = 0;
    private String query;

    private Runnable loadLocal = new Runnable() {
        @Override
        public void run() {
            List<PackageInfo> packages = getActivity().getPackageManager().getInstalledPackages(0);
            List<AppBean> temp = new ArrayList<>();
            for (int i = 0; i < packages.size(); i++) {
                PackageInfo packageInfo = packages.get(i);
                ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {

                } else {
                    AppBean appInfo = new AppBean();
                    appInfo.setVersCode(packageInfo.versionCode);
                    appInfo.setApkName(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString());
                    appInfo.setPkgName(packageInfo.packageName);
                    appInfo.setVersNum(packageInfo.versionName);
                    appInfo.setIcon(packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager()));
                    temp.add(appInfo);
                }
            }
            Message msg = new Message();
            msg.what = 5;
            msg.obj = temp;
            handler.sendMessage(msg);
        }
    };

    private Runnable loadHomepage = new Runnable() {
        @Override
        public void run() {
            presenter.loadHomepage(pager);
        }
    };
    
    private Runnable loadSearch = new Runnable() {
        @Override
        public void run() {
            presenter.loadSearch(query, pager);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    swipeRefresh.setRefreshing(false);
                    break;
                case 1:
                    swipeRefresh.setRefreshing(true);
                    break;
                case 2:
                    break;
                case 3:
                    if (msg.obj != null) {
                        list = (List<AppBean>) msg.obj;
                        adapter = new AppGridAdapter(getActivity(), list);
                        rvList.setAdapter(adapter);
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
                case 4:
                    if (msg.obj != null && load == 1) {
                        int size = list.size();
                        list.addAll((List<AppBean>) msg.obj);
                        for (int i = 0; i < ((List<AppBean>) msg.obj).size(); i++) {
                            adapter.notifyItemInserted(size + i);
                        }
                        load = 0;
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
                case 5:
                    if (msg.obj != null) {
                        list.addAll((List<AppBean>) msg.obj);
                        Collections.sort(list, AppComparator);
                        adapter = new AppGridAdapter(getActivity(), list);
                        rvList.setAdapter(adapter);
                    }
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        }
    };

    Comparator AppComparator = new Comparator() {
        public int compare(Object obj1, Object obj2) {
            AppBean app1 = (AppBean) obj1;
            AppBean app2 = (AppBean) obj2;
            if (app1.getApkName().compareToIgnoreCase(app2.getApkName()) < 0)
                return -1;
            else if (app1.getApkName().compareToIgnoreCase(app2.getApkName()) == 0)
                return 0;
            else if (app1.getApkName().compareToIgnoreCase(app2.getApkName()) > 0)
                return 1;
            return 0;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    @Override
    public void fetchData() {
        initData();
    }

    private void initData() {
        type = getArguments().getInt("id", 0);
        if (type == 0) {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                showSnackbar(rvList, "未找到存储卡");
            } else {
                new Thread(loadLocal).start();
                swipeRefresh.setRefreshing(true);
            }
        } else if (type == 1) {
            new Thread(loadHomepage).start();
        } else if (type == 2) {
            query = getArguments().getString("query");
            new Thread(loadSearch).start();
        }
    }

    private void initView(View view) {

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessageDelayed(msg, 800);
            }
        });
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        adapter = new AppGridAdapter(getActivity(), list);
        adapter.setHasStableIds(true);
        rvList.setAdapter(adapter);
        rvList.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && last + 1 == adapter.getItemCount()) {
                    load = 1;
                    pager += 1;
                    if (type == 1) {
                        new Thread(loadHomepage).start();
                    } else if (type == 2) {
                        query = getArguments().getString("query");
                        new Thread(loadSearch).start();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                last = getMax(layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]));
            }
        });
    }

    private int getMax(int[] arr) {
        int len = arr.length;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < len; i++) {
            max = Math.max(max, arr[i]);
        }
        return max;
    }

    @Override
    protected AppPresenter onCreatePresenter() {
        return new AppPresenter(this);
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
    public void showHomepage(List<AppBean> list) {
        Message msg = new Message();
        if (pager == 1) {
            msg.what = 3;
        } else {
            msg.what = 4;
        }
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showSearch(List<AppBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showDetail(AppBean bean) {

    }

    @Override
    public void showDownload(String log) {

    }

    @Override
    public void showUpgrade(List<AppBean> list) {

    }
}
