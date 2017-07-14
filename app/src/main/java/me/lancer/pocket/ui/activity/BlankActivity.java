package me.lancer.pocket.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.base.activity.BaseActivity;
import me.lancer.pocket.info.mvp.book.fragment.BookFragment;
import me.lancer.pocket.info.mvp.code.fragment.CodeFragment;
import me.lancer.pocket.info.mvp.comic.fragment.ComicFragment;
import me.lancer.pocket.info.mvp.game.fragment.GameFragment;
import me.lancer.pocket.info.mvp.joke.fragment.JokeFragment;
import me.lancer.pocket.info.mvp.movie.fragment.MovieFragment;
import me.lancer.pocket.info.mvp.music.fragment.MusicFragment;
import me.lancer.pocket.info.mvp.news.fragment.NewsFragment;
import me.lancer.pocket.info.mvp.novel.fragment.NovelFragment;
import me.lancer.pocket.info.mvp.photo.fragment.PhotoFragment;
import me.lancer.pocket.info.mvp.video.fragment.VideoFragment;
import me.lancer.pocket.tool.mvp.contacts.fragment.ContactsFragment;

public class BlankActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        Bundle bundle = new Bundle();
        switch (getIntent().getIntExtra("index", 0)) {
            case -3:
                bundle.putInt(getString(R.string.index), 0);
                currentFragment = new ContactsFragment();
                break;
            case -2:
                bundle.putInt(getString(R.string.index), 1);
                currentFragment = new ContactsFragment();
                break;
            case -1:
                bundle.putInt(getString(R.string.index), 2);
                currentFragment = new ContactsFragment();
                break;
            case 1:
                bundle.putInt(getString(R.string.index), 0);
                currentFragment = new NewsFragment();
                break;
            case 2:
                bundle.putInt(getString(R.string.index), 0);
                currentFragment = new JokeFragment();
                break;
            case 3:
                bundle.putInt(getString(R.string.index), 0);
                currentFragment = new BookFragment();
                break;
            case 4:
                bundle.putInt(getString(R.string.index), 0);
                currentFragment = new MusicFragment();
                break;
            case 5:
                bundle.putInt(getString(R.string.index), 0);
                currentFragment = new MovieFragment();
                break;
            case 6:
                bundle.putInt(getString(R.string.index), 0);
                currentFragment = new PhotoFragment();
                break;
            case 7:
                bundle.putInt(getString(R.string.index), 0);
                currentFragment = new NovelFragment();
                break;
            case 8:
                bundle.putInt(getString(R.string.index), 0);
                currentFragment = new ComicFragment();
                break;
            case 9:
                bundle.putInt(getString(R.string.index), 0);
                currentFragment = new VideoFragment();
                break;
            case 10:
                bundle.putInt(getString(R.string.index), 0);
                currentFragment = new GameFragment();
                break;
            case 11:
                bundle.putInt(getString(R.string.index), 0);
                currentFragment = new CodeFragment();
                break;
        }
        currentFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_main, currentFragment).commit();
        invalidateOptionsMenu();
    }

    public void initDrawer(Toolbar toolbar) {
        if (toolbar != null) {
            toolbar.inflateMenu(R.menu.menu_search);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }
}
