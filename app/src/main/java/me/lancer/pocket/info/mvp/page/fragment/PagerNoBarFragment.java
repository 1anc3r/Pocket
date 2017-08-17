package me.lancer.pocket.info.mvp.page.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.mvp.base.fragment.BaseFragment;
import me.lancer.pocket.ui.application.Params;

public class PagerNoBarFragment extends BaseFragment {

    private String link;
    private ImageView imageView;

    public static PagerNoBarFragment newInstance(String link) {
        Bundle args = new Bundle();
        args.putString("link", link);
        PagerNoBarFragment fragment = new PagerNoBarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        link = getArguments().getString("link");
        return inflater.inflate(R.layout.fragment_page_no_bar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = (ImageView) view.findViewById(R.id.imageView);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewCompat.setTransitionName(imageView, Params.TRANSITION_PIC);
        Glide.with(this).load(link).into(imageView);
    }
}

