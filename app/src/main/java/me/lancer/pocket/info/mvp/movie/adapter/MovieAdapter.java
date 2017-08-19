package me.lancer.pocket.info.mvp.movie.adapter;

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
import me.lancer.pocket.info.mvp.movie.MovieBean;
import me.lancer.pocket.info.mvp.movie.activity.MovieDetailActivity;
import me.lancer.pocket.ui.application.Params;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<MovieBean> list;
    private Context context;

    public MovieAdapter(Context context, List<MovieBean> list) {
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
                star = Float.parseFloat(list.get(position).getStar()) / 2;
                viewHolder.tvContent.setText(list.get(position).getContent());
            }
            viewHolder.rbRating.setRating(star);
            ViewCompat.setTransitionName(viewHolder.ivImg, Params.TRANSITION_PIC);
            Glide.with(context).load(list.get(position).getImg()).into(viewHolder.ivImg);
            viewHolder.cvMedimu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getSubTitle() == null || list.get(position).getSubTitle().equals("")) {
                        Intent intent = new Intent();
                        intent.putExtra("title", list.get(position).getMainTitle());
                        intent.putExtra("type", 0);
                        intent.putExtra("link", list.get(position).getMainLink());
                        intent.putExtra("img", list.get(position).getImg());
                        intent.setClass(context, MovieDetailActivity.class);
                        context.startActivity(intent);
//                        MovieDetailActivity.startActivity((Activity) context, 0, list.get(position).getMainTitle(), list.get(position).getImg(), list.get(position).getMainLink(), viewHolder.ivImg);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("title", list.get(position).getMainTitle());
                        intent.putExtra("type", 1);
                        intent.putExtra("link", list.get(position).getSubLink());
                        intent.putExtra("img", list.get(position).getImg());
                        intent.setClass(context, MovieDetailActivity.class);
                        context.startActivity(intent);
//                        MovieDetailActivity.startActivity((Activity) context, 1, list.get(position).getMainTitle(), list.get(position).getImg(), list.get(position).getSubLink(), viewHolder.ivImg);
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

        public CardView cvMedimu;
        public ImageView ivImg;
        public TextView tvTitle, tvContent;
        public RatingBar rbRating;

        public ViewHolder(View rootView) {
            super(rootView);
            cvMedimu = (CardView) rootView.findViewById(R.id.cv_medimu);
            ivImg = (ImageView) rootView.findViewById(R.id.iv_cover);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            tvContent = (TextView) rootView.findViewById(R.id.htv_content);
            rbRating = (RatingBar) rootView.findViewById(R.id.rb_medimu);
        }
    }
}
