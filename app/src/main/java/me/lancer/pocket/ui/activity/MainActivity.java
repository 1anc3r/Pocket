package me.lancer.pocket.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.fragment.CollectFragment;
import me.lancer.pocket.ui.fragment.MainFragment;
import me.lancer.pocket.ui.mvp.base.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private LinearLayout container;
    private Fragment currentFragment;

    private int currentIndex;
    private long exitTime;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = (LinearLayout) findViewById(R.id.container);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        initNavigationViewHeader();
        initFragment(savedInstanceState);
//        setNeedsMenuKey();
    }

    private void initFragment(Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        currentFragment = new MainFragment();
        bundle.putInt(getString(R.string.index), 0);
        currentFragment.setArguments(bundle);
        switchContent(currentFragment);
    }

    public void initDrawer(Toolbar toolbar) {
        if (toolbar != null) {
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
            actionBarDrawerToggle.syncState();
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
        }
    }

    private void initNavigationViewHeader() {
        navigationView = (NavigationView) findViewById(R.id.nv_main);
        disableNavigationViewScrollbars(navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationItemSelected());
    }

    public void switchContent(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_main, fragment).commit();
        invalidateOptionsMenu();
    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showSnackbar(container, "再按一次退出应用");
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

    private void setNeedsMenuKey() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return;
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            try {
                int flags = WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null);
                getWindow().addFlags(flags);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Method setNeedsMenuKey = Window.class.getDeclaredMethod("setNeedsMenuKey", int.class);
                setNeedsMenuKey.setAccessible(true);
                int value = WindowManager.LayoutParams.class.getField("NEEDS_MENU_SET_TRUE").getInt(null);
                setNeedsMenuKey.invoke(getWindow(), value);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    class NavigationItemSelected implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            Bundle bundle = new Bundle();
            drawerLayout.closeDrawers();
            switch (menuItem.getItemId()) {
                case R.id.navigation_item_1:
                    currentIndex = 0;
                    menuItem.setChecked(true);
                    currentFragment = new MainFragment();
                    bundle.putInt(getString(R.string.index), currentIndex);
                    currentFragment.setArguments(bundle);
                    switchContent(currentFragment);
                    return true;
                case R.id.navigation_item_2:
                    currentIndex = 0;
                    menuItem.setChecked(true);
                    currentFragment = new CollectFragment();
                    bundle.putInt(getString(R.string.index), currentIndex);
                    currentFragment.setArguments(bundle);
                    switchContent(currentFragment);
                    return true;
                case R.id.navigation_setting:
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent().setClass(MainActivity.this, SettingActivity.class));
                        }
                    }, 180);
                    return true;
                default:
                    return true;
            }
        }
    }
}
