package me.lancer.pocket.info.mvp.news.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.news.NewsBean;
import me.lancer.pocket.info.mvp.news.activity.NewsDetailActivity;
import me.lancer.pocket.ui.application.Params;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private static final int TYPE_CONTENtoolbar = -1;
    private static final int TYPE_CONTENT_SMALL = 0;
    private static final int TYPE_TITLE = 1;

    private List<NewsBean> list;
    private Context context;

    public NewsAdapter(Context context, List<NewsBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_large, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            if (getItemViewType(position) == TYPE_TITLE) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
                viewHolder.tvTitle.setText(list.get(position).getTitle());
                viewHolder.tvTitle.setTextSize(20);
                viewHolder.tvTitle.setGravity(Gravity.CENTER);
                viewHolder.ivCover.setVisibility(View.GONE);
            } else if (getItemViewType(position) == TYPE_CONTENT_SMALL) {
                viewHolder.tvTitle.setText(list.get(position).getTitle());
                ViewCompat.setTransitionName(viewHolder.ivCover, Params.TRANSITION_PIC);
                Glide.with(context).load(list.get(position).getImg()).into(viewHolder.ivCover);
                viewHolder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewsDetailActivity.startActivity((Activity) context, list.get(position).getId(), list.get(position).getTitle(), list.get(position).getImg(), list.get(position).getLink(), viewHolder.ivCover);
                    }
                });
            } else if (getItemViewType(position) == TYPE_CONTENtoolbar) {
                viewHolder.tvTitle.setText(list.get(position).getTitle());
                ViewCompat.setTransitionName(viewHolder.ivCover, Params.TRANSITION_PIC);
                Glide.with(context).load(list.get(position).getImg()).into(viewHolder.ivCover);
                viewHolder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewsDetailActivity.startActivity((Activity) context, list.get(position).getId(), list.get(position).getTitle(), list.get(position).getImg(), list.get(position).getLink(), viewHolder.ivCover);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getType() == -1) {
            return TYPE_CONTENtoolbar;
        } else if (list.get(position).getType() == 0) {
            return TYPE_CONTENT_SMALL;
        } else if (list.get(position).getType() == 1) {
            return TYPE_TITLE;
        }
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cv;
        public ImageView ivCover;
        public TextView tvTitle;

        public ViewHolder(View rootView) {
            super(rootView);
            cv = (CardView) rootView.findViewById(R.id.cv);
            ivCover = (ImageView) rootView.findViewById(R.id.iv_cover);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        }
    }
}
