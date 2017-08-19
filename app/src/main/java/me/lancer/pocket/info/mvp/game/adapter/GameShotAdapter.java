package me.lancer.pocket.info.mvp.game.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.photo.activity.PhotoGalleryActivity;
import me.lancer.pocket.ui.application.Params;

public class GameShotAdapter extends RecyclerView.Adapter<GameShotAdapter.ViewHolder> {

    private List<String> list;
    private Context context;

    public GameShotAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_game_shot, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            ViewCompat.setTransitionName(viewHolder.ivCover, Params.TRANSITION_PIC);
            Glide.with(context).load(list.get(position)).into(viewHolder.ivCover);
            viewHolder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("gallery", (ArrayList<String>) list);
                    intent.putExtra("position", position);
                    intent.setClass(context, PhotoGalleryActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cv;
        public ImageView ivCover;

        public ViewHolder(View rootView) {
            super(rootView);
            cv = (CardView) rootView.findViewById(R.id.cv);
            ivCover = (ImageView) rootView.findViewById(R.id.iv_cover);
        }
    }
}
