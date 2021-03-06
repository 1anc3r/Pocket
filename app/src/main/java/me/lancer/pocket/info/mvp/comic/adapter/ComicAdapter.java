package me.lancer.pocket.info.mvp.comic.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.chapter.activity.ChapterActivity;
import me.lancer.pocket.info.mvp.comic.ComicBean;
import me.lancer.pocket.ui.application.Params;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder> {

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_CONTENT = 1;

    private List<ComicBean> list;
    private Context context;

    public ComicAdapter(Context context, List<ComicBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comic, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            ComicBean bean = list.get(position);
            if (getItemViewType(position) == TYPE_CONTENT) {
                viewHolder.tvTitle.setText(bean.getTitle());
                if (bean.getCategory().equals("")) {
                    viewHolder.tvCategory.setVisibility(View.GONE);
                } else {
                    viewHolder.tvCategory.setText(bean.getCategory());
                }
                ViewCompat.setTransitionName(viewHolder.ivCover, Params.TRANSITION_PIC);
                Glide.with(context).load(list.get(position).getCover()).into(viewHolder.ivCover);
                viewHolder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ChapterActivity.startActivity((Activity) context, list.get(position).getLink(), list.get(position).getCover(), list.get(position).getTitle(), list.get(position).getCategory(), viewHolder.ivCover);
                    }
                });
            } else if (getItemViewType(position) == TYPE_TITLE) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
                viewHolder.tvTitle.setText(bean.getTitle());
                viewHolder.tvCategory.setVisibility(View.GONE);
                viewHolder.ivCover.setVisibility(View.GONE);
                viewHolder.ivIconLeft.setVisibility(View.VISIBLE);
                viewHolder.ivIconRight.setVisibility(View.VISIBLE);
                if (!bean.getCover().equals("")) {
                    Glide.with(context).load(list.get(position).getCover()).into(viewHolder.ivIconLeft);
                    Glide.with(context).load(list.get(position).getCover()).into(viewHolder.ivIconRight);
                }
                if (bean.getLink() != null && !bean.getLink().equals("")) {
                    viewHolder.container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.putExtra("title", list.get(position).getTitle());
                            intent.putExtra("link", list.get(position).getLink());
                            intent.setClass(context, ChapterActivity.class);
                            context.startActivity(intent);
                        }
                    });
                }
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
            return TYPE_TITLE;
        } else if (list.get(position).getType() == 1) {
            return TYPE_CONTENT;
        }
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public LinearLayout container;
        public ImageView ivCover, ivIconLeft, ivIconRight;
        public TextView tvTitle, tvCategory;

        public ViewHolder(View rootView) {
            super(rootView);
            cardView = (CardView) rootView.findViewById(R.id.cv);
            container = (LinearLayout) rootView.findViewById(R.id.container);
            ivCover = (ImageView) rootView.findViewById(R.id.iv_cover);
            ivIconLeft = (ImageView) rootView.findViewById(R.id.iv_icon_left);
            ivIconRight = (ImageView) rootView.findViewById(R.id.iv_icon_right);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            tvCategory = (TextView) rootView.findViewById(R.id.tv_category);
        }
    }
}
