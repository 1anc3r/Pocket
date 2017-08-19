package me.lancer.pocket.info.mvp.joke.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.joke.JokeBean;
import me.lancer.pocket.info.mvp.photo.activity.PhotoGalleryActivity;
import me.lancer.pocket.ui.application.Params;

public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.ViewHolder> {

    private static final int TYPE_TEXT = 0;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VIDEO = 2;

    private List<JokeBean> list;
    private Context context;

    public JokeAdapter(Context context, List<JokeBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_great, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            if (getItemViewType(position) == TYPE_TEXT) {
                viewHolder.tvText.setText(list.get(position).getText());
                viewHolder.ivCover.setVisibility(View.GONE);
            } else if (getItemViewType(position) == TYPE_IMAGE) {
                if (list.get(position).getText() == null || list.get(position).getText().equals("")) {
                    viewHolder.tvText.setVisibility(View.GONE);
                } else {
                    viewHolder.tvText.setText(list.get(position).getText());
                }
                ViewCompat.setTransitionName(viewHolder.ivCover, Params.TRANSITION_PIC);
                Glide.with(context).load(list.get(position).getImg()).into(viewHolder.ivCover);
                viewHolder.ivCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> gallery = new ArrayList<String>();
                        for (JokeBean bean : list) {
                            gallery.add(bean.getImg());
                        }
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra("gallery", gallery);
                        intent.putExtra("position", position);
                        intent.setClass(context, PhotoGalleryActivity.class);
                        context.startActivity(intent);
                    }
                });
            } else if (getItemViewType(position) == TYPE_VIDEO) {
                viewHolder.tvText.setText(list.get(position).getText());

            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getType() == 0) {
            return TYPE_TEXT;
        } else if (list.get(position).getType() == 1) {
            return TYPE_IMAGE;
        } else if (list.get(position).getType() == 2) {
            return TYPE_VIDEO;
        }
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cvJoke;
        public ImageView ivCover;
        public TextView tvText;

        public ViewHolder(View rootView) {
            super(rootView);
            cvJoke = (CardView) rootView.findViewById(R.id.cv);
            ivCover = (ImageView) rootView.findViewById(R.id.iv_cover);
            tvText = (TextView) rootView.findViewById(R.id.tv_title);
        }
    }
}
