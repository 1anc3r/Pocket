package me.lancer.pocket.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.base.activity.BaseActivity;
import me.lancer.pocket.ui.fragment.BlankFragment;
import me.lancer.pocket.ui.fragment.SettingFragment;

public class MainActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment currentFragment;
    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigationView();
        initFragment(savedInstanceState);
    }

    private void initFragment(Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        currentFragment = new BlankFragment();
        bundle.putInt(getString(R.string.index), 0);
        currentFragment.setArguments(bundle);
        switchContent(currentFragment);
    }

    private void initNavigationView() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Bundle bundle = new Bundle();
            switch (item.getItemId()) {
                case R.id.navigation_tool:
                    currentFragment = new BlankFragment();
                    bundle.putInt(getString(R.string.index), 0);
                    currentFragment.setArguments(bundle);
                    switchContent(currentFragment);
                    return true;
                case R.id.navigation_info:
                    currentFragment = new BlankFragment();
                    bundle.putInt(getString(R.string.index), 1);
                    currentFragment.setArguments(bundle);
                    switchContent(currentFragment);
                    return true;
                case R.id.navigation_mine:
                    currentFragment = new SettingFragment();
                    bundle.putInt(getString(R.string.index), 2);
                    currentFragment.setArguments(bundle);
                    switchContent(currentFragment);
                    return true;
            }
            return false;
        }
    };

    public void switchContent(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_main, fragment).commit();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast(this, "再按一次退出应用");
                exitTime = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
