package me.lancer.pocket.info.mvp.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.music.MusicBean;
import me.lancer.pocket.info.mvp.music.activity.MusicDetailActivity;
import me.lancer.pocket.ui.application.Params;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private List<MusicBean> list;
    private Context context;

    public MusicAdapter(Context context, List<MusicBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_medimu, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            viewHolder.tvTitle.setText(list.get(position).getMainTitle());
            String temp = "";
            float star = 0;
            if (list.get(position).getSubTitle() != null && !list.get(position).getSubTitle().equals("")) {
                temp = " 评论 " + list.get(position).getSubTitle();
                star = Float.parseFloat(list.get(position).getStar());
                viewHolder.tvContent.setText(list.get(position).getAuthor() + temp);
            } else {
                temp = list.get(position).getMainTitle().split(" - ")[1];
                viewHolder.tvTitle.setText(temp);
                temp = list.get(position).getMainTitle().split(" - ")[0];
                viewHolder.tvContent.setText(temp);
                star = Float.parseFloat(list.get(position).getStar()) / 2;
            }
            viewHolder.rbRating.setRating(star);
            ViewCompat.setTransitionName(viewHolder.ivCover, Params.TRANSITION_PIC);
            Glide.with(context).load(list.get(position).getImg()).into(viewHolder.ivCover);
            viewHolder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getSubTitle() == null || list.get(position).getSubTitle().equals("")) {
                        Intent intent = new Intent();
                        intent.putExtra("title", list.get(position).getMainTitle());
                        intent.putExtra("type", 0);
                        intent.putExtra("link", list.get(position).getMainLink());
                        intent.putExtra("img", list.get(position).getImg());
                        intent.setClass(context, MusicDetailActivity.class);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("title", list.get(position).getMainTitle());
                        intent.putExtra("type", 1);
                        intent.putExtra("link", list.get(position).getSubLink());
                        intent.putExtra("img", list.get(position).getImg());
                        intent.setClass(context, MusicDetailActivity.class);
                        context.startActivity(intent);
                    }
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
        public TextView tvTitle, tvContent;
        public RatingBar rbRating;

        public ViewHolder(View rootView) {
            super(rootView);
            cv = (CardView) rootView.findViewById(R.id.cv);
            ivCover = (ImageView) rootView.findViewById(R.id.iv_cover);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            tvContent = (TextView) rootView.findViewById(R.id.htv_content);
            rbRating = (RatingBar) rootView.findViewById(R.id.rb_medimu);
        }
    }
}
